package seven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post implements Serializable {
    private static int counter = 1; // Счётчик для уникальных ID
    private final int id;
    private final User author;
    private final String content;
    private int likes;
    private int reposts;
    private final Date createdAt;
    private final List<String> comments;
    private static final long serialVersionUID = 1L; // Для обеспечения совместимости сериализации

    public Post(User author, String content) {
        this.id = counter++;
        this.author = author;
        this.content = content;
        this.likes = 0;
        this.reposts = 0;
        this.createdAt = new Date();
        this.comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public int getLikes() {
        return likes;
    }

    public int getReposts() {
        return reposts;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void like() {
        likes++;
    }

    public void repost() {
        reposts++;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Post.counter = counter;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setReposts(int reposts) {
        this.reposts = reposts;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public boolean removeComment(String comment) {
        return comments.remove(comment);
    }

    // Метод для восстановления уникального ID после сериализации
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt(counter);
    }

    // Метод для восстановления уникального ID после десериализации
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        counter = in.readInt();
    }

    @Override
    public String toString() {
        return String.format("Post{id=%d, author='%s', content='%s', likes=%d, reposts=%d, createdAt=%s}",
                id, author.getName(), content, likes, reposts, createdAt);
    }
}
