package vn.hoidanit.laptopshop;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
	public String index() {
		return "Hello World Vu quoc Huy";
	}

	@GetMapping("/user")
	public String usePage() {
		return "only user can access this page";
	}

	@GetMapping("/admin")
	public String adminPage() {
		return "only admin can access this page";
	}

}
