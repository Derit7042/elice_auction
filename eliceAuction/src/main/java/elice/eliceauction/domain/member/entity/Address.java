package elice.eliceauction.domain.member.entity;

import jakarta.persistence.Embeddable;

@Embeddable
class Address {
    private String postalCode;
    private String address1;
    private String address2;
}
