package com.example.payment.service;

import com.example.payment.client.ProductServiceClient;
import com.example.payment.dto.PaymentRequest;
import com.example.payment.dto.PaymentResponse;
import com.example.payment.model.Payment;
import com.example.payment.model.Product;
import com.example.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProductServiceClient productServiceClient;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ProductServiceClient productServiceClient) {
        this.paymentRepository = paymentRepository;
        this.productServiceClient = productServiceClient;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // Проверяем существование продукта
        Product product = productServiceClient.getProduct(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + request.getProductId());
        }

        // Проверяем достаточность средств
        if (product.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds. Available: " + product.getBalance() + ", Required: " + request.getAmount());
        }

        // Создаем платеж
        Payment payment = new Payment(request.getProductId(), request.getAmount(), request.getDescription());
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        
        Payment savedPayment = paymentRepository.save(payment);
        return new PaymentResponse(savedPayment);
    }

    public PaymentResponse getPayment(Long paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isPresent()) {
            return new PaymentResponse(payment.get());
        }
        throw new RuntimeException("Payment not found with ID: " + paymentId);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByProductId(Long productId) {
        return paymentRepository.findByProductId(productId).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    // Метод для получения продуктов пользователя через платежный сервис
    public List<Product> getUserProducts(Long userId) {
        return productServiceClient.getProductsByUserId(userId);
    }

    // Метод для получения продукта по ID через платежный сервис
    public Product getProduct(Long productId) {
        return productServiceClient.getProduct(productId);
    }

    // Метод для получения продукта по номеру счета через платежный сервис
    public Product getProductByAccountNumber(String accountNumber) {
        return productServiceClient.getProductByAccountNumber(accountNumber);
    }
} 