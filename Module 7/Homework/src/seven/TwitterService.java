package seven;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TwitterService {

    private final List<Post> posts = new ArrayList<>();

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

    // Метод для лайка поста
    public boolean likePost(int postId) {
        Optional<Post> post = posts.stream().filter(p -> p.getId() == postId).findFirst();
        if (post.isPresent()) {
            post.get().like();
            System.out.println("Пост лайкнут!");
            return true;
        }
        System.out.println("Пост с таким ID не найден.");
        return false;
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


}
