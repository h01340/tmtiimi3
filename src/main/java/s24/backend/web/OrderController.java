package s24.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import s24.backend.domain.Customer;
import s24.backend.domain.CustomerRepository;
import s24.backend.domain.Order;
import s24.backend.domain.OrderRepository;
import s24.backend.domain.ProductRepository;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderrepo;

    @Autowired
    private CustomerRepository customerrepo;

    @Autowired
    private ProductRepository productrepo;

    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("customers", customerrepo.findAll());
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String showCustomerOrders(@PathVariable("id") Long id, Model model) {
        Customer c = customerrepo.findById(id).get();
        model.addAttribute("orders", orderrepo.findByCustomer(c));
        return "customerorder";
    }

    @GetMapping("/addorder")
    public String addOrder(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("products", productrepo.findAll());
        model.addAttribute("customers", customerrepo.findAll());
        return "addorder";
    }

    @PostMapping("/saveorder")
    public String saveOrder(@ModelAttribute("order") Order order) {
        order.setStatus("Tilaus käsittelyssä");
        orderrepo.save(order);
        return "redirect:orders";
    }

    @GetMapping("/deliverorder")
    public String deliverOrder(@RequestParam(name = "id") Long id) {
        Long returnid = orderrepo.findById(id).get().getCustomer().getCustomerid();
        Order order = orderrepo.findById(id).get();
        order.setStatus("Tilaus toimitettu");
        orderrepo.save(order);
        return "redirect:orders/" + returnid;
    }

    @GetMapping("/cancelorder")
    public String cancelOrder(@RequestParam(name = "id") Long id) {
        Long returnid = orderrepo.findById(id).get().getCustomer().getCustomerid();
        Order order = orderrepo.findById(id).get();
        order.setStatus("Tilaus peruttu");
        orderrepo.save(order);
        return "redirect:orders/" + returnid;
    }
}
