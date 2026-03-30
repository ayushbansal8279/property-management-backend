@Data
public class ExpenseRequestDto {

    private UUID ledgerId;
    private UUID paidByPartnerId;
    private BigDecimal totalAmount;

    private List<UUID> splitPartnerIds; // optional

    private String splitType; // EQUAL / CUSTOM

    private Map<UUID, BigDecimal> customSplit; // if CUSTOM

    private String description;
}