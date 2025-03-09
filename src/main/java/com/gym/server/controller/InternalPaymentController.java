package com.gym.server.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.gym.server.dto.InternalPayment.InternalPaymentDTO;
import com.gym.server.dto.Zarinpal.PaymentResponseDto;
import com.gym.server.dto.Zarinpal.RequestDto;
import com.gym.server.dto.Zarinpal.verify.VerifyReqDto;
import com.gym.server.dto.Zarinpal.verify.VerifyResDto;
import com.gym.server.model.InternalPayment;
import com.gym.server.repository.InternalPaymentRepository;
import com.gym.server.repository.UserRepository;
import com.gym.server.service.ZarinpalService;
import com.gym.server.service.impliment.InternalPaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internalpayment")
public class InternalPaymentController {

    @Value("${app.zarinpal.paymentUrl}")
    private String paymentUrl;


    @Value("${app.ip}")
    private String serverIp;


    @Value("${app.zarinpal.redirectUrl}")
    private String redirectUrl;



    private final UserRepository userRepository;



    private final InternalPaymentServiceImpl internalPaymentService;
    private final InternalPaymentRepository internalPaymentRepository;
    private final ZarinpalService zarinpalService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(internalPaymentService.getAll(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> add(@RequestBody InternalPaymentDTO req){
        try {
            return new ResponseEntity<>(internalPaymentService.add(req), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }





    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/{accountId}/list")
    public ResponseEntity<?> listPayments(@PathVariable Long accountId){
        return new ResponseEntity<>(internalPaymentService.retrieve(accountId), HttpStatus.OK);
    }





    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id){

            return new ResponseEntity<>(internalPaymentService.delete(id), HttpStatus.OK);
    }

    @Scheduled(fixedRate = 3600000*24)  // This runs every 24hour
    public void runEveryMinute() {
        Iterable<InternalPayment> list = internalPaymentRepository.findAll(); // Assume InternalPayment is your entity class

        for (InternalPayment payment : list) {
            if ("pending".equals(payment.getStatus())) {  // Corrected the method call
                internalPaymentRepository.deleteById(payment.getId());
            }
        }
    }





    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
    InternalPayment res =  internalPaymentService.getById(id);
    return new ResponseEntity<>(res,HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/payment/{id}")
    public ResponseEntity<?> payment(@PathVariable Long id) {
        InternalPayment res =  internalPaymentService.getById(id);
        res.getTransactionId();

        RequestDto req = new RequestDto();
        req.setAmount(res.getAmount().toString());
        req.setMobile("09917403979");
        req.setEmail("ali@gmail.com");

        PaymentResponseDto response= zarinpalService.paymentRequest(req,redirectUrl+res.getTransactionId(),"پرداخت شهریه");
        String link = paymentUrl+"/"+response.getData().getAuthority().toString();
        Map<String,Object> map = new HashMap<>();
        map.put("message","لینک درگاه با موفقیت ایجاد شد");
        map.put("link",link);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


//    @PreAuthorize("hasAnyRole('ROLE_USER')")
@GetMapping("/callback/{transactionId}")
public String callBack(@PathVariable String transactionId,
                       @RequestParam String Authority,
                       @RequestParam String Status,
                       Model model) {

    if ("NOK".equals(Status)) {
        model.addAttribute("message", "تراکنش ناموفق");
        model.addAttribute("redirectLink", redirectUrl);
        return "paymentResult";
    }

    try {
        InternalPayment payment = internalPaymentService.getByTransactionId(transactionId);
        VerifyReqDto req = new VerifyReqDto();
        req.setAuthority(Authority);
        req.setAmount(payment.getAmount().toString());

        boolean isVerified = zarinpalService.verify(req);
        if (isVerified) {
            internalPaymentService.callBack(transactionId, Status);
            model.addAttribute("message", "تراکنش با موفقیت انجام شد");
        } else {
            model.addAttribute("message", "تراکنش ناموفق");
        }
        model.addAttribute("redirectLink", redirectUrl);
        return "paymentResult";
    } catch (Exception e) {
//        logger.error("Error processing callback for transactionId: " + transactionId, e);
        model.addAttribute("message", "خطا در پردازش درخواست");
        model.addAttribute("error", e.getMessage());
        return "paymentResult";
    }
}



}
