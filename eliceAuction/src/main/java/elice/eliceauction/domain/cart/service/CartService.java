package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.member.entity.Member;

import java.util.List;

public interface CartService {

    void createCart(Member member);
    List<CartItem> getCarts(Member member);

    CartItem getCart(Member member, Long productId);

    boolean isEmpty(Member member);

    void clear(Member member);

    void add(Member member, Long productId);

    CartItem delete(Member member, Long productId);

    int getCount(Member member);
}
