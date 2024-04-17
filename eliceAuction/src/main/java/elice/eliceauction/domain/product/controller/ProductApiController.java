package elice.eliceauction.domain.product.controller;

import elice.eliceauction.domain.product.dto.ProductDto;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    // GET
    /*********스웨거 어노테이션**********/
    @Operation(summary = "등록된 상품 목록 조회", description = "유저들이 등록한 상품들을 한 페이지에 보이도록 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 목록 페이지 조회 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
    })
    /*********스웨거 어노테이션**********/
    @GetMapping
    public List<Product> index() {
        return productService.index();
    }

    /*********스웨거 어노테이션**********/
    @Operation(summary = "상품 상세 조회", description = "유저가 등록한 상품의 상세페이지를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 상세 페이지 조회 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
    })
    @Parameter(name = "id", description = "상품의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/{id}")
    public Product show(@PathVariable("id") Long id) {
        return productService.show(id);
    }

    // POST
    @Operation(summary = "상품 등록", description = "유저가 상품을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 등록 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
    })
    @Parameter(name = "file", description = "상품 이미지 파일")
    @Parameter(name = "title", description = "상품 이름")
    @Parameter(name = "brief", description = "상품 설명")
    @Parameter(name = "price", description = "상품 가격")
    @PostMapping
    public ResponseEntity<Product> create(@RequestParam("file") MultipartFile file,
                                          @RequestParam("title") String title,
                                          @RequestParam("brief") String brief,
                                          @RequestParam("price") Long price) {
        // 파일 처리 로직
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String fileName = file.getOriginalFilename();

        // 나머지 필요한 데이터 처리 로직
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String pictureLink = "https://i.ibb.co/tp4LQLr/1.jpg";

        ProductDto dto = new ProductDto();
        dto.setTitle(title);
        dto.setBrief(brief);
        dto.setPrice(price);
        dto.setWatchCount(0L);
        dto.setDate(currentDateTime);
        dto.setPictureLink(pictureLink);

        Product created = productService.create(dto);

        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // PATCH
    /*********스웨거 어노테이션**********/
    @Operation(summary = "상품 수정", description = "유저가 등록한 상품을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 수정 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
    })
    @Parameter(name = "id", description = "상품의 고유 id 번호")
    @Parameter(name = "dto", description = "상품 수정 시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
    @PatchMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Long id, @RequestBody ProductDto dto) {
        Product updated = productService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE
    /*********스웨거 어노테이션**********/
    @Operation(summary = "상품 삭제", description = "유저가 등록한 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
    })
    @Parameter(name = "id", description = "상품의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        Product deleted = productService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}