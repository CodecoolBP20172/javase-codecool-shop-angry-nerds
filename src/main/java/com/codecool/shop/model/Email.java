package com.codecool.shop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    public static void sendEmail(Order order) {

        final String username = "codecoolshop.angrynerds@gmail.com";
        final String password = "Honolulu27";
        Map userData = order.getUserData();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("peter.bernath.27@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse((String) userData.get("E-mail")));
            message.setSubject("Order No. " + order.getId() + " Confirmation");
            message.setText("Dear " + userData.get("Name") + ","
                    + "\n\n Thank You for your order! Your order details can be found below in this e-mail."
                    + "\n\n Order number: " + order.getId()
                    + "\n Name: " + userData.get("Name")
                    + "\n E-mail: " + userData.get("E-mail")
                    + "\n Phone Number: " + userData.get("Phone Number")
                    + "\n Billing Address: " + userData.get("Billing Address")
                    + "\n Billing City: " + userData.get("Billing City")
                    + "\n Billing Zipcode: " + userData.get("Billing Zipcode")
                    + "\n Billing Country: " + userData.get("Billing Country")
                    + "\n Shipping Address: " + userData.get("Shipping Address")
                    + "\n Shipping City: " + userData.get("Shipping City")
                    + "\n Shipping Zipcode: " + userData.get("Shipping Zipcode")
                    + "\n Shipping Country: " + userData.get("Shipping Country")
                    + "\n\n Ordered items: \n"
                    + convertOrder(order.getOrder())
                    + "\n\n We wish You merry christmas and hope You will shop with us next time!"
                    + "\n Best regards,"
                    + "\n\n Your CodeCool Shop");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private static String convertOrder(Map<Product, Integer> map) {
        String orderString = "";
        int total = 0;
        for (Product name: map.keySet()){

            String key = name.getName();
            String value = map.get(name).toString();
            String price = name.getPrice();
            orderString += "\n " + value + "x   " + key + "       " + price;
            total += Integer.parseInt(name.getPrice().substring(0, name.getPrice().length() - 6)) * Integer.parseInt(value);
        }
        orderString += "\n --------------------------------------------------";
        orderString += "\n Total amount paid:        " + String.valueOf(total) + " USD";
        return orderString;
    }

}
