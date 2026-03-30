@Data
public class ShareTransferRequestDto {

    private UUID fromPartnerId;
    private UUID toPartnerId;
    private BigDecimal percentage;
    private BigDecimal amountPaid; 
    private String comments;
}