@RestController
@RequestMapping("/ownership")
@RequiredArgsConstructor
public class PropertyOwnershipController {

    private final PropertyOwnershipService service;

    // 💰 INVEST
    @PostMapping("/invest/{propertyId}")
    public void invest(@PathVariable UUID propertyId,
                       @RequestBody InvestmentRequestDto req) {

        service.addInvestment(propertyId, req.getPartnerId(), req.getAmount());
    }

    // 🔄 TRANSFER
    @PostMapping("/transfer/{propertyId}")
public void transfer(@PathVariable UUID propertyId,
                     @RequestBody ShareTransferRequestDto req) {

    service.transferShare(
            propertyId,
            req.getFromPartnerId(),
            req.getToPartnerId(),
            req.getPercentage(),
            req.getAmountPaid()
    );
}

    // 📋 GET
    @GetMapping("/{propertyId}")
    public List<OwnershipResponseDto> get(@PathVariable UUID propertyId) {
        return service.getOwnership(propertyId);
    }
}