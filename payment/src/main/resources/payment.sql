-- Create index on column
CREATE UNIQUE INDEX receipt_uq ON mr_payment (receipt_id);
commit;

CREATE INDEX paymentid_uq ON mr_payment (payment_id);
commit;