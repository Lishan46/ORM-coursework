package org.example;

import org.example.config.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class main {
    public static void main(String[] args) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
    }
}
