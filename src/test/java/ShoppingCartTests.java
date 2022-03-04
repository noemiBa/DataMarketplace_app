//import myapp.model.CartItem;
//import myapp.model.Data_assets;
//import myapp.model.Users;
//import myapp.repository.CartItemRepository;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.annotation.Rollback;
//
//import javax.xml.crypto.Data;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false) //commit changes to database
//
//public class ShoppingCartTests {
//
//    @Autowired
//    private CartItemRepository cartRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    public void testAddOneCartItem() {
//        Data_assets product = entityManager.find(Data_assets.class, 1); //adding Finity dataset
//        Users user = entityManager.find(Users.class, 2);
//
//        CartItem newItem = new CartItem();
//        newItem.setUser(user);
//        newItem.setProduct(product);
//        newItem.setQuantity(1);
//
//        CartItem savedCartItem = cartRepository.save(newItem);
//
//        assertTrue(savedCartItem.getId() > 0);
//    }
//
//    @Test
//    public void testGetCartItemsByUser() {
//        Users user = new Users();
//        user.setId(2);
//
//        List<CartItem> cartItems = cartRepository.findByUser(user);
//
//        assertEquals(1, cartItems.size());
//    }
//
//
//}
