package az.example.eventsapp.util;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailHelper {

    private final JavaMailSender javaMailSender;

    public void sendTicketConfirmationEmail(String to, String subject, String body, List<byte[]> pdfAttachments) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            for (int i = 0; i < pdfAttachments.size(); i++) {
                DataSource dataSource = new ByteArrayDataSource(pdfAttachments.get(i), "application/pdf");
                helper.addAttachment("ticket" + (i + 1) + ".pdf", dataSource);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(message);
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Your OTP for password reset is: " + otp);
        javaMailSender.send(message);
    }
}
