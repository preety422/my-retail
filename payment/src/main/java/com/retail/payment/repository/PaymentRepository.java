package com.retail.payment.repository;

import com.retail.payment.entity.MRPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<MRPayment, String> {

    MRPayment findMRPaymentByOrderId(String orderId);
    MRPayment findMRPaymentByReceiptId(String receiptId);

    }