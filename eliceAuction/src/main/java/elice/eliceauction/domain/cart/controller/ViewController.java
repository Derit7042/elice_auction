package elice.eliceauction.domain.cart.controller;

import elice.eliceauction.domain.cart.entity.CartResponseDto;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
//@CrossOrigin(origins = "http://localhost:8080")
public class ViewController {
    @GetMapping
    public String cartViewPage(Model model) {

        // TODO: 서버로 HTTP 요청 보내고 데이터 받기
//        WebClient webclient = WebClient.create("http://localhost:8080/api");
//
//        Mono<List<CartResponseDto>> response = webclient.get()
//                .uri("/cart/1")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<List<CartResponseDto>>() {});
//
//        List<CartResponseDto> dtos = response.block();
//        for (CartResponseDto dto : dtos) {
//            System.out.println("-----------------RESPONSE TEST-------------------------------");
//            System.out.println("dto.getTitle() = " + dto.getTitle());
//            System.out.println("dto.getPrice() = " + dto.getPrice());
//            System.out.println("dto.getPictureLink() = " + dto.getPictureLink());
//            System.out.println("-----------------RESPONSE TEST-------------------------------");
//        }

        return "cart/cart.html";
    }
}
