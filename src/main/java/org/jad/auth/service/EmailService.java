package org.jad.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordChangedNotification(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jaddaoui.salim@gmail.com"); // important

        message.setTo(to);
        message.setSubject("🔐 Changement de mot de passe - OCP");
        message.setText("Votre mot de passe a été modifié avec succès.\n\n"
                + "Si ce n'est pas vous, veuillez contacter immédiatement l'administration.");

        mailSender.send(message);
    }

    public void envoyerEmailAvecPieceJointe(String to, String subject, String text, File file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom("jaddaoui.salim@gmail.com"); // important
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment(file.getName(), file);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
}
