package src.main.java.org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.StudentAttendanceDto;
import org.example.util.AttendanceNameUtil;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@WebServlet("/attendance")
public class StudentAttendanceServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String selectedGroup = req.getParameter("group");

        List<StudentAttendanceDto> list = getStudentsFromDB(selectedGroup); // передаём фильтр
        List<String> allGroups = getAllGroupNames();

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Attendance</title></head><body>");
        out.println("<h2>Посещение лекций</h2>");

        // --- SELECT с группами ---
        out.println("<form method='GET' action='/ServletPractice/attendance'>");
        out.println("Группа: <select name='group' onchange='this.form.submit()'>");
        out.println("<option value=''>Все группы</option>");
        for (String group : allGroups) {
            if (group.equals(selectedGroup)) {
                out.println("<option selected>" + group + "</option>");
            } else {
                out.println("<option>" + group + "</option>");
            }
        }
        out.println("</select></form>");

        // --- Форма добавления ---
        out.println("<form action='/ServletPractice/attendance' method='POST'>");
        out.println("ФИО: <input type='text' name='name' required><br>");
        out.println("Группа: <input type='text' name='groupName' required><br>");
        out.println("Посетил: <select name='isAttended'><option value='true'>Да</option><option value='false'>Нет</option></select><br>");
        out.println("<input type='submit' value='Добавить'>");
        out.println("</form>");

        // --- Таблица студентов ---
        out.println("<table border='1'>");
        out.println("<tr><th>ФИО</th><th>Группа</th><th>Посетил</th><th>Действие</th></tr>");
        for (StudentAttendanceDto student : list) {
            out.println("<tr>");
            out.println("<td>" + student.getName() + "</td>");
            out.println("<td>" + student.getGroupName() + "</td>");
            out.println("<td>" + AttendanceNameUtil.fromBooleanToString(student.isAttended()) + "</td>");
            out.println("<td><form method='POST' action='/ServletPractice/attendance'>" +
                    "<input type='hidden' name='deleteName' value='" + student.getName() + "'>" +
                    "<input type='hidden' name='groupName' value='" + student.getGroupName() + "'>" +
                    "<input type='submit' name='action' value='Удалить'>" +
                    "</form></td>");
            out.println("</tr>");
        }
        out.println("</table></body></html>");
    }

    private List<StudentAttendanceDto> getStudentsFromDB(String groupName) {
        List<StudentAttendanceDto> result = new ArrayList<>();

        String sql = "SELECT s.name, g.name AS group_name, s.is_attended " +
                "FROM students s JOIN groups g ON s.group_id = g.id";

        if (groupName != null && !groupName.isEmpty()) {
            sql += " WHERE g.name = ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (groupName != null && !groupName.isEmpty()) {
                stmt.setString(1, groupName);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StudentAttendanceDto dto = StudentAttendanceDto.builder()
                        .name(rs.getString("name"))
                        .groupName(rs.getString("group_name"))
                        .isAttended(rs.getBoolean("is_attended"))
                        .build();
                result.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private int getOrCreateGroupId(Connection conn, String groupName) throws SQLException {
        String selectSql = "SELECT id FROM groups WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setString(1, groupName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        String insertSql = "INSERT INTO groups (name) VALUES (?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, groupName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        throw new SQLException("Не удалось получить или создать group_id");
    }

    private List<String> getAllGroupNames() {
        List<String> groups = new ArrayList<>();
        String sql = "SELECT name FROM groups ORDER BY name";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                groups.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("Удалить".equals(action)) {
            String nameToDelete = req.getParameter("deleteName");
            String groupName = req.getParameter("groupName");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM students WHERE name = ? AND group_id = (SELECT id FROM groups WHERE name = ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nameToDelete);
                    stmt.setString(2, groupName);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            // добавление студента
            String name = req.getParameter("name");
            String groupName = req.getParameter("groupName");
            boolean isAttended = Boolean.parseBoolean(req.getParameter("isAttended"));

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                int groupId = getOrCreateGroupId(conn, groupName);
                String sql = "INSERT INTO students (name, group_id, is_attended) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, groupId);
                    pstmt.setBoolean(3, isAttended);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        resp.sendRedirect("/ServletPractice/attendance");
    }
}
