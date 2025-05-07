package com.example.hopdrop;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private Session mSession;
    private String mEmail, mSubject, mMessage;

    public JavaMailAPI(Context context, String email, String subject, String message) {
        mContext = context;
        mEmail = email;
        mSubject = subject;
        mMessage = message;

        // Email credentials (sender email and app password)
        final String fromEmail = "YOUR_EMAIL@gmail.com"; // change this
        final String fromPassword = "YOUR_APP_PASSWORD"; // use Gmail App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        mSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Message message = new MimeMessage(mSession);
            message.setFrom(new InternetAddress("YOUR_EMAIL@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mEmail));
            message.setSubject(mSubject);
            message.setText(mMessage);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Toast.makeText(mContext, "Verification email sent!", Toast.LENGTH_SHORT).show();
    }
}
