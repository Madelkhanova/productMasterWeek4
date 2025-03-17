package seven;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TwitterService {
    private static final String FILE_NAME = "posts.ser";  // Файл для сериализации
    private final List<Post> posts = new ArrayList<>();

    public TwitterService() {
        loadPosts();
    }

    public void initializePosts() {
        posts.add(new Post(new User("Alice"), "Привет, мир!"));
        posts.add(new Post(new User("Bob"), "Сегодня отличный день!"));
        posts.add(new Post(new User("Charlie"), "Люблю программировать на Java."));
        System.out.println("Добавлены стартовые посты.");
    }

    // Метод для написания поста
    public void createPost(User user, String content) {
        Post post = new Post(user, content);
        posts.add(post);
        System.out.println("Пост добавлен!");
    }

    // Лайк поста
    public void likePost(int postId) {
        posts.stream()
                .filter(post -> post.getId() == postId)
                .findFirst()
                .ifPresent(post -> {
                    post.like();
                    savePosts();
                });
    }

    // Метод для репоста поста
    public boolean repostPost(int postId) {
        Optional<Post> post = posts.stream().filter(p -> p.getId() == postId).findFirst();
        if (post.isPresent()) {
            post.get().repost();
            System.out.println("Пост репостнут!");
            return true;
        }
        System.out.println("Пост с таким ID не найден.");
        return false;
    }

    // Метод для отображения всех постов
    public void showAllPosts() {
        System.out.println("Все посты:");
        posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .forEach(System.out::println);
    }

    // Метод для отображения популярных постов
    public void showPopularPosts(int count) {
        System.out.println("Популярные посты:");
        posts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()))
                .limit(count)
                .forEach(System.out::println);
    }

    // Метод для отображения постов текущего пользователя
    public void showUserPosts(User user) {
        System.out.println("Мои посты:");
        posts.stream()
                .filter(p -> p.getAuthor().equals(user))
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .forEach(System.out::println);
    }
    // Сохранение постов в файл
    private void savePosts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(posts);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении постов: " + e.getMessage());
        }
    }

    // Загрузка постов из файла
    private void loadPosts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<Post> loadedPosts = (List<Post>) ois.readObject();
            posts.addAll(loadedPosts);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке постов: " + e.getMessage());
        }
    }

    // Метод для добавления комментария
    public void addComment(int postId, String comment) {
        Optional<Post> post = posts.stream().filter(p -> p.getId() == postId).findFirst();
        if (post.isPresent()) {
            post.get().addComment(comment);
            savePosts();
            System.out.println("Комментарий добавлен!");
        } else {
            System.out.println("Пост с таким ID не найден.");
        }
    }

    // Метод для удаления поста
    public boolean deletePost(int postId) {
        Optional<Post> postToDelete = posts.stream().filter(p -> p.getId() == postId).findFirst();
        if (postToDelete.isPresent()) {
            posts.remove(postToDelete.get());
            savePosts();
            System.out.println("Пост удален!");
            return true;
        }
        System.out.println("Пост с таким ID не найден.");
        return false;
    }
}
