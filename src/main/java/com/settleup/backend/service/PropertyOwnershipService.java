@Service
@RequiredArgsConstructor
public class PropertyOwnershipService {

    private final PropertyOwnershipRepository ownershipRepository;
    private final PropertyRepository propertyRepository;
    private final PartnerRepository partnerRepository;

    // 💰 INVESTMENT
    public void addInvestment(UUID propertyId, UUID partnerId, BigDecimal amount) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        PropertyOwnership ownership = ownershipRepository
                .findByPropertyId(propertyId)
                .stream()
                .filter(o -> o.getPartner().getId().equals(partnerId))
                .findFirst()
                .orElse(new PropertyOwnership());

        ownership.setProperty(property);
        ownership.setPartner(partner);

        BigDecimal newInvestment = ownership.getInvestmentAmount() == null
                ? amount
                : ownership.getInvestmentAmount().add(amount);

        ownership.setInvestmentAmount(newInvestment);

        ownershipRepository.save(ownership);

        recalculateOwnership(propertyId);
    }

    // 📊 RECALCULATE %
    private void recalculateOwnership(UUID propertyId) {

        List<PropertyOwnership> list = ownershipRepository.findByPropertyId(propertyId);

        BigDecimal total = list.stream()
                .map(PropertyOwnership::getInvestmentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (PropertyOwnership o : list) {
            BigDecimal percent = o.getInvestmentAmount()
                    .divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            o.setOwnershipPercentage(percent);
        }

        ownershipRepository.saveAll(list);
    }

    // 🔄 SHARE TRANSFER
    public void transferShare(UUID propertyId, UUID fromId, UUID toId, BigDecimal percent) {

        List<PropertyOwnership> list = ownershipRepository.findByPropertyId(propertyId);

        PropertyOwnership from = list.stream()
                .filter(o -> o.getPartner().getId().equals(fromId))
                .findFirst()
                .orElseThrow();

        PropertyOwnership to = list.stream()
                .filter(o -> o.getPartner().getId().equals(toId))
                .findFirst()
                .orElseGet(() -> {
                    PropertyOwnership newOne = new PropertyOwnership();
                    newOne.setProperty(from.getProperty());
                    newOne.setPartner(partnerRepository.findById(toId).orElseThrow());
                    newOne.setInvestmentAmount(BigDecimal.ZERO);
                    return newOne;
                });

        BigDecimal transferAmount = from.getInvestmentAmount()
                .multiply(percent)
                .divide(BigDecimal.valueOf(100));

        from.setInvestmentAmount(from.getInvestmentAmount().subtract(transferAmount));
        to.setInvestmentAmount(to.getInvestmentAmount().add(transferAmount));

        ownershipRepository.save(from);
        ownershipRepository.save(to);

        recalculateOwnership(propertyId);
    }

    // 📋 GET OWNERSHIP
    public List<OwnershipResponseDto> getOwnership(UUID propertyId) {

        return ownershipRepository.findByPropertyId(propertyId)
                .stream()
                .map(o -> new OwnershipResponseDto(
                        o.getPartner().getName(),
                        o.getInvestmentAmount(),
                        o.getOwnershipPercentage()
                ))
                .toList();
    }
}