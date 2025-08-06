package repository;

import jakarta.persistence.*;
import model.User;
import dto.ApiResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UserDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public ApiResponse<?> createUser(String username, String password) {
        EntityManager em = emf.createEntityManager();

        try {
            // Check if user already exists
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
            query.setParameter("username", username);

            if (query.getSingleResult() > 0) {
                return new ApiResponse<>(false, "Username already exists", 409);
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(hashPassword(password));

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            tx.commit();

            return new ApiResponse<>(true, "User created successfully", user);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    public Optional<User> findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}
