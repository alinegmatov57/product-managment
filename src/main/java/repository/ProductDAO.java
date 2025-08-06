package repository;

import dto.ApiResponse;
import jakarta.persistence.*;
import model.Product;
import model.ProductType;
import model.enums.PRODUCT_STATUS;

import java.util.List;

public class ProductDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");


    public ApiResponse<?> create(Product dto) {
        EntityManager entityManager = emf.createEntityManager();
        ProductType type = entityManager.find(ProductType.class, dto.getType().getId());
        if (type == null)
            return new ApiResponse<>(false,"ProductType not found",404);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Product product = new Product();
        product.setName(dto.getName());
        product.setStatus(dto.getStatus());
        product.setType(type);
        entityManager.persist(product);
        transaction.commit();
        entityManager.close();
        return new ApiResponse<>(true,"Product created",product);
    }

    public ApiResponse<?> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        Product p = em.find(Product.class, id);
        if (p == null)
            return new ApiResponse<>(false, "Product not found",404);
        em.close();
        return new ApiResponse<>(p,true);
    }

    public ApiResponse<?> findAll(int page, int size) {
        EntityManager em = emf.createEntityManager();
        List<Product> list = em.createQuery("SELECT p FROM Product p", Product.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
        em.close();
        return new ApiResponse<>(list,true);
    }

    public ApiResponse<?> update(Integer id, Product dto) {
        EntityManager entityManager = emf.createEntityManager();

        Product existingProduct = entityManager.find(Product.class, id);
        if (existingProduct == null)
            return new ApiResponse<>(false, "Product not found", 404);

        ProductType type = entityManager.find(ProductType.class, dto.getType().getId());
        if (type == null)
            return new ApiResponse<>(false, "ProductType not found", 404);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        existingProduct.setName(dto.getName());
        existingProduct.setStatus(dto.getStatus());
        existingProduct.setType(type);

        transaction.commit();
        entityManager.close();

        return new ApiResponse<>(true, "Product updated", existingProduct);
    }



    public ApiResponse<?> delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        Product p = em.find(Product.class, id);
        if (p != null) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(p);
            tx.commit();
        }
        em.close();
        return new ApiResponse<>(true, "Product deleted successfully");
    }
}
