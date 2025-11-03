package com.ecom_application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecom_application.dto.OrderRequest;
import com.ecom_application.entity.Cart;
import com.ecom_application.entity.OrderAddress;
import com.ecom_application.entity.ProductOrder;
import com.ecom_application.entity.User;
import com.ecom_application.repository.CartRepository;
import com.ecom_application.repository.ProductOrderRepository;
import com.ecom_application.util.OrderStatus;

@Service
public class ProductOrderService {


	@Autowired
	private ProductOrderRepository porepo;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserService userRepository;
	
	@Autowired
	private CartService cartService;

	
	public String saveOrder(Integer userId, OrderRequest request) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		
		String orderId = ("ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000)).toString().substring(0,12);
		for(Cart cart : carts) {
			ProductOrder order = new ProductOrder();
			int deliveryFee = ((cart.getProduct().getPrice() * cart.getQuantity()) - cartService.discountPrice(cart.getProduct().getPrice() * cart.getQuantity())) > 499 ? 0 : 80;
			order.setOrderDate(LocalDate.now());
			order.setDeliveryDate(LocalDate.now().plusDays(3));
			order.setOrderId(orderId);
			
			order.setProduct(cart.getProduct());
			order.setPrice((cart.getProduct().getPrice() * cart.getQuantity()) - cartService.discountPrice(cart.getProduct().getPrice() * cart.getQuantity()) + deliveryFee);
			
			order.setUser(userRepository.findUserById(userId).get());
			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			
			order.setQuantity(cart.getQuantity());
			order.setPaymentType(request.getPaymentType());
			
			OrderAddress address = new OrderAddress();
			address.setFirstName(request.getFirstName());
			address.setLastName(request.getLastName());
			address.setEmail(request.getEmail());
			address.setMobile(request.getMobile());
			address.setAddress(request.getAddress());
			address.setCity(request.getCity());
			address.setState(request.getState());
			address.setPincode(request.getPincode());
			
			order.setOrderAddress(address);
			resetCart(cart.getUser());
			porepo.save(order);
		}
		return orderId;
	}
	
	private void resetCart(User user) {
		cartRepository.deleteByUser(user);
	}
	
	public List<ProductOrder> findAllOrdersByUser(Integer userId) {
		return porepo.findAllByUserId(userId);
	}
	
	public void updateOrderStatus(Integer id) {
		ProductOrder order = porepo.findById(id).get();
		if(LocalDate.now().isEqual(order.getOrderDate().plusDays(1)) && order.getStatus() != "Cancelled") {
			order.setStatus(OrderStatus.PRODUCT_PACKED.getName());
		}
		if(order.getStatus() != "Cancelled" && (LocalDate.now().isEqual(order.getOrderDate().plusDays(2)) || LocalDate.now().isEqual(order.getOrderDate().plusDays(1)))) {
			order.setStatus(OrderStatus.SHIPPED.getName());
		}
		if(LocalDate.now().isEqual(order.getOrderDate().plusDays(4)) && order.getStatus() != "Cancelled") {
			order.setStatus(OrderStatus.OUT_FOR_DELIVERY.getName());
		}
		if(LocalDate.now().isAfter(order.getOrderDate().plusDays(5)) && order.getStatus() != "Cancelled") {
			order.setStatus(OrderStatus.DELIVERED.getName());
		}
		porepo.save(order);
	}
	
	public void cancelOrder(Integer id) {
		ProductOrder order = porepo.findById(id).get();
		order.setStatus(OrderStatus.CANCEL.getName());
		porepo.save(order);
		System.out.println("Cancel service called");
	}
	
	public Optional<ProductOrder> findByOrderById(int id) {
		return porepo.findById(id);
	}
	
	public List<ProductOrder> findListOrdersBySellerId(int id) {
		return porepo.findByProductSellerId(id);
	}
	
}
