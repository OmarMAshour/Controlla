package Services;

import android.os.AsyncTask;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public  class RetrieveFeedTask extends AsyncTask<String,Void,String> {
    Session session ;
    String Receiver;
    public  RetrieveFeedTask(Session session, String Receiver){
        this.session = session;
        this.Receiver = Receiver;
    }
    public String doInBackground(String ... strings) {
        javax.mail.Message msg = new MimeMessage(session);
       String x = strings[0];
       String y = strings[1];

        try {
            msg.setFrom(new InternetAddress("mohdosama962@gmail.com"));
            ((MimeMessage) msg).setRecipients(Message.RecipientType.TO,this.Receiver);
            msg.setSubject("Controlla - SOS Email");
            msg.setSentDate(new Date());
            // set plain text message
            msg.setText("help your Friend he is in the current location    " + "http://maps.google.com/maps?q="+x+","+y);
        } catch (MessagingException e) {
            e.printStackTrace();
        }



        // *** BEGIN CHANGE
        // sends the e-mail

        try {
            Transport t = session.getTransport("smtp");
            t.connect("mohdosama962@gmail.com", "7219961234");
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}