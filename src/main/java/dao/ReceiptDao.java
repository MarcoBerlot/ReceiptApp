package dao;

import api.ReceiptResponse;
import api.TagResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.ReceiptTagRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.RECEIPT_TAG;


public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }


    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }
    public void insert_tag(String tag, int id) {
        ReceiptTagRecord existingTag = dsl
                .selectFrom(RECEIPT_TAG)
                .where(RECEIPT_TAG.TAG.equal(tag).and(RECEIPT_TAG.RECEIPT_ID.equal(id)))
                .fetchAny();
        if(existingTag==null){
               dsl .insertInto(RECEIPT_TAG, RECEIPT_TAG.RECEIPT_ID, RECEIPT_TAG.TAG)
                .values(id, tag)
                .returning(RECEIPTS.ID)
                .fetchOne();
        }else{
            dsl.delete(RECEIPT_TAG)
                    .where(RECEIPT_TAG.TAG.equal(tag).and(RECEIPT_TAG.RECEIPT_ID.equal(id)))
                    .execute();

        }


        return ;
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }
    public List<ReceiptsRecord> getAllTags(String tag) {
     return dsl .selectDistinct()
                .from(RECEIPTS)
                .rightOuterJoin(RECEIPT_TAG)
                .on(RECEIPT_TAG.RECEIPT_ID.equal(RECEIPTS.ID))
                .where(RECEIPT_TAG.TAG.equal(tag))
                .fetchInto(RECEIPTS);
    }
    public List<ReceiptTagRecord> getAllTagsId() {
        return dsl .selectFrom(RECEIPT_TAG)
                .fetch();
    }

}
