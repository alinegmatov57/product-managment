package repository;

import dto.ApiResponse;
import jakarta.persistence.*;
import model.ProductType;


import java.util.List;

public class ProductTypeDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public ApiResponse<?> create(ProductType dto) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(pt) FROM ProductType pt WHERE LOWER(pt.typeName) = LOWER(:name)",
                    Long.class
            );
            query.setParameter("name", dto.getTypeName());
            Long count = query.getSingleResult();

            if (count > 0)
                return new ApiResponse<>(false, "This ProductType already exists", 409);

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(dto);
            tx.commit();

            return new ApiResponse<>(true, "ProductType created", dto);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error occurred: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    public ApiResponse<?> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            List<ProductType> list = em.createQuery("SELECT pt FROM ProductType pt", ProductType.class)
                    .getResultList();
            return new ApiResponse<>(true, "All ProductTypes", list);
        } finally {
            em.close();
        }
    }

    public ApiResponse<?> getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            ProductType productType = em.find(ProductType.class, id);
            if (productType == null)
                return new ApiResponse<>(false, "ProductType not found", 404);
            return new ApiResponse<>(true, "Found", productType);
        } finally {
            em.close();
        }
    }

    public ApiResponse<?> update(Long id, ProductType dto) {
        EntityManager em = emf.createEntityManager();

        try {
            ProductType existing = em.find(ProductType.class, id);
            if (existing == null)
                return new ApiResponse<>(false, "ProductType not found", 404);

            // Check duplicate (but skip if updating same value)
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(pt) FROM ProductType pt WHERE LOWER(pt.typeName) = LOWER(:name) AND pt.id <> :id",
                    Long.class
            );
            query.setParameter("name", dto.getTypeName());
            query.setParameter("id", id);
            Long count = query.getSingleResult();

            if (count > 0)
                return new ApiResponse<>(false, "This ProductType already exists", 409);

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            existing.setTypeName(dto.getTypeName());
            em.merge(existing);
            tx.commit();

            return new ApiResponse<>(true, "ProductType updated", existing);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error occurred: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    public ApiResponse<?> delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            model.ProductType productType = em.find(ProductType.class, id);
            if (productType == null)
                return new ApiResponse<>(false, "ProductType not found", 404);

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(productType);
            tx.commit();

            return new ApiResponse<>(true, "ProductType deleted");
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error occurred: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
}
