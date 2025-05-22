package lk.javainstitute.savoryhub.model;

import android.util.Log;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import android.os.AsyncTask;

public class Mail {
    public void sendVerificationEmail(String to, String code) {
        new EmailTask(to, code).execute();
    }

    private static class EmailTask extends AsyncTask<Void, Void, Void> {
        private final String to;
        private final String code;

        public EmailTask(String to, String code) {
            this.to = to;
            this.code = code;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sendEmailInternal(to, code);
            } catch (MessagingException e) {
                Log.e("EmailVerification", "Failed to send email: " + e.getMessage(), e);
            }
            return null;
        }

        private void sendEmailInternal(String to, String code) throws MessagingException {
            String from = "nithushishavindi0@gmail.com";
            final String username = "nithushishavindi0@gmail.com";
            final String password = "gypq xjve xrdf qrhm";

            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("SavoryHub Admin Verification Code");
            message.setText("Your Verification Code: " + code);

            Transport.send(message);
            Log.i("EmailVerification", "Sent message successfully...");
        }
    }
}
