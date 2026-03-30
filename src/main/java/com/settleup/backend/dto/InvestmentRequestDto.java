@Data
public class ShareTransferRequestDto {

    private UUID fromPartnerId;
    private UUID toPartnerId;

    private BigDecimal percentage;   // 🔥 % of property
    private BigDecimal amountPaid;   // 🔥 deal value
}