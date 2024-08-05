package az.example.eventsapp.service;

import az.example.eventsapp.entity.PaymentEntity;
import az.example.eventsapp.entity.TicketEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.exception.PaymentFailedException;
import az.example.eventsapp.exception.TicketNotFoundException;
import az.example.eventsapp.exception.UserNotFoundException;
import az.example.eventsapp.repository.PaymentRepository;
import az.example.eventsapp.repository.TicketRepository;
import az.example.eventsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public PaymentEntity processPayment(Long userId, Long ticketId, double amount, String paymentMethod) {
        log.info("Processing payment for user ID: {} and ticket ID: {} with amount: {} and payment method: {}",
                userId, ticketId, amount, paymentMethod);

        boolean paymentSuccess = mockPaymentGateway(amount, paymentMethod);

        if (!paymentSuccess) {
            log.error("Payment failed for user ID: {} and ticket ID: {} with amount: {}", userId, ticketId, amount);
            throw new PaymentFailedException();
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException();
                });

        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    log.error("Ticket not found with ID: {}", ticketId);
                    return new TicketNotFoundException();
                });

        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(paymentMethod);
        payment.setUser(user);
        payment.setTicket(ticket);

        paymentRepository.save(payment);

        log.info("Payment processed successfully for user ID: {} and ticket ID: {} with amount: {}", userId, ticketId, amount);
        return payment;
    }

    private boolean mockPaymentGateway(double amount, String paymentMethod){
        log.info("Mock payment gateway called with amount: {} and payment method: {}", amount, paymentMethod);
        return true;
    }
}
