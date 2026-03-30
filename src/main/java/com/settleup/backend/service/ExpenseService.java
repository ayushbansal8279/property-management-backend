@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final TransactionRepository transactionRepository;
    private final ExpenseSplitRepository splitRepository;
    private final PartnerRepository partnerRepository;
    private final LedgerRepository ledgerRepository;

    public void addExpense(ExpenseRequestDto req) {

        Ledger ledger = ledgerRepository.findById(req.getLedgerId()).orElseThrow();
        Partner payer = partnerRepository.findById(req.getPaidByPartnerId()).orElseThrow();

        // 🔥 Create transaction
        Transaction tx = new Transaction();
        tx.setType("EXPENSE");
        tx.setLedger(ledger);
        tx.setFromPartnerId(req.getPaidByPartnerId());
        tx.setAmount(req.getTotalAmount());
        tx.setDescription(req.getDescription());
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);

        List<Partner> involvedPartners;

        if (req.getSplitPartnerIds() == null || req.getSplitPartnerIds().isEmpty()) {
            involvedPartners = partnerRepository.findByLedgerId(req.getLedgerId());
        } else {
            involvedPartners = req.getSplitPartnerIds()
                    .stream()
                    .map(id -> partnerRepository.findById(id).orElseThrow())
                    .toList();
        }

        // 🔥 EQUAL SPLIT
        if ("EQUAL".equalsIgnoreCase(req.getSplitType())) {

            BigDecimal perHead = req.getTotalAmount()
                    .divide(BigDecimal.valueOf(involvedPartners.size()));

            for (Partner p : involvedPartners) {
                saveSplit(tx, p, perHead);
            }

        } else if ("CUSTOM".equalsIgnoreCase(req.getSplitType())) {

            req.getCustomSplit().forEach((partnerId, amount) -> {
                Partner p = partnerRepository.findById(partnerId).orElseThrow();
                saveSplit(tx, p, amount);
            });
        }
    }

    private void saveSplit(Transaction tx, Partner partner, BigDecimal amount) {

        ExpenseSplit split = new ExpenseSplit();
        split.setTransaction(tx);
        split.setPartner(partner);
        split.setAmount(amount);

        splitRepository.save(split);
    }
}