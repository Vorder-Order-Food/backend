package com.vorder.payment_service.controller;

import com.vorder.payment_service.dto.OrderDto;
import com.vorder.payment_service.dto.request.OrderRequest;
import com.vorder.payment_service.dto.response.PaymentResponse;
import com.vorder.payment_service.repository.http.OrderClient;
import com.vorder.payment_service.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;
    OrderClient orderClient;

    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler( HttpServletResponse response, @RequestParam String vnp_ResponseCode) throws IOException {

        if ("00".equals(vnp_ResponseCode)) {
            response.sendRedirect("http://localhost:3000/payment-success");

        } else {
            response.sendRedirect("http://localhost:3000/payment-failure");
        }
    }


    @PostMapping("/pay-by-vn-pay")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody OrderRequest req,
                                                       HttpServletRequest request
    ) throws Exception {

        OrderDto order =  orderClient.createOrder(req);

        String bankCode = "NCB";

        PaymentResponse res = paymentService.createVNPayment(request, order.getTotalPrice(), bankCode);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

}
