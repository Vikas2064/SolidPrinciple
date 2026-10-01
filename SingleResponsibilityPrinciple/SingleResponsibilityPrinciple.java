
/// here  we can see that the following class have diffent reason to change. calculate logic chagne the state of class changes, printInvoice logic changes
/// or save to database logic changes the state of class chagnes

class Invoice {

    public void calculateTotal() {
        // calculate invoice total
    }

    public void printInvoice() {
        // print invoice
    }

    public void saveToDatabase() {
        // save invoice to database
    }
}

/// we can create a different class for this like this
///
///
/// to calculate invoice
class Invoice {

    public void calculateTotal() {
        // calculate invoice total
    }
}


/// to print Invoice
class InvoicePrinter {

    public void print(Invoice invoice) {
        // print invoice
    }
}
/// to save into DB
class InvoiceRepository {

    public void save(Invoice invoice) {
        // save invoice to database
    }
}