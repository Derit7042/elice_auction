package elice.eliceauction.domain.cart.controller;

import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.entity.CartResponseDto;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "cart", description = "장바구니 API")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    @Autowired
    public CartController(final CartService cartService, final MemberService memberService) {
        this.cartService = cartService;
        this.memberService = memberService;
    }
    
    // 장바구니 목록 불러오기
    /*********스웨거 어노테이션**********/
    @Operation(summary = "장바구니 조회", description = "유저 id(memberId)를 이용하여 해당 유저의 장바구니에 담긴 상품 목록을 불러옵니다. 만약 id가 null이면 로그인 한 계정을 기준으로 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "장바구니 조회 성공",
                        content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
            })
    @Parameter(name = "memberId", description = "사용자의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/{memberId}")
    public ResponseEntity<List<CartResponseDto>> getCarts(@AuthenticationPrincipal Member member,
                                                          @PathVariable(name = "memberId", required = false) Long memberId
                                                        ) throws Exception {
        if(member == null) {// 로그인 상태가 아닌 경우
            System.out.println("--------------------------MEMBER IS NULL------------------");
            member = memberService.findMemberById(memberId);
        }

        System.out.println("============================-member = " + member.getId());

        List<CartItem> cartItems = cartService.getCarts(member);

        // CartItem -> DTO로 변환
        List<CartResponseDto> cartResponseDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartResponseDtos.add(CartItem.toDto(cartItem));
        }

        return ResponseEntity.ok(cartResponseDtos);
    }
    
    // 장바구니에 상품 추가
    /*********스웨거 어노테이션**********/
    @Operation(summary = "장바구니에 상품 추가", description = "유저 id(memberId)를 이용하여 해당 유저의 장바구니에 상품id(productId)에 해당하는 상품을 추가합니다. 만약 id가 null이면 로그인 한 계정을 기준으로 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 추가 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
    })
    @Parameter(name = "memberId", description = "사용자의 고유 id 번호")
    @Parameter(name = "productId", description = "상품 id")
    /*********스웨거 어노테이션**********/
    @PostMapping("/{memberId}")
    public ResponseEntity<CartResponseDto> addCartItem(@AuthenticationPrincipal Member member,
                                                       @PathVariable(name = "memberId", required = false) Long memberId,
                                                       @RequestParam("productId") Long productId
                                                        ) throws Exception{
        if(member == null) {// 로그인 상태가 아닌 경우
            member = memberService.findMemberById(memberId);
        }

        cartService.add(member, productId);
        CartItem cartItem = cartService.getCart(member, productId);

        // CartItem -> DTO로 변환
        CartResponseDto dto = CartItem.toDto(cartItem);

        return ResponseEntity.ok(dto);
    }
    
    // 장바구니에서 특정 상품 삭제
    /*********스웨거 어노테이션**********/
    @Operation(summary = "장바구니에서 상품 삭제", description = "유저 id(memberId)를 이용하여 해당 유저의 장바구니에 상품id(productId)에 해당하는 상품을 삭제합니다.  만약 id가 null이면 로그인 한 계정을 기준으로 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 삭제 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
    })
    @Parameter(name = "memberId", description = "사용자의 고유 id 번호")
    @Parameter(name = "productId", description = "상품 id")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{memberId}")
    public ResponseEntity<CartResponseDto> deleteCartItem(@AuthenticationPrincipal Member member,
                                                          @PathVariable(name = "memberId", required = false) Long memberId,
                                                          @RequestParam("productId") Long productId
                                                            ) throws Exception{
        if(member == null) {// 로그인 상태가 아닌 경우
            member = memberService.findMemberById(memberId);
        }

        CartItem deleted = cartService.delete(member, productId);


        // CartItem -> DTO로 변환
        CartResponseDto cartResponseDto =CartItem.toDto(deleted);


        return ResponseEntity.ok(cartResponseDto);
    }
}
