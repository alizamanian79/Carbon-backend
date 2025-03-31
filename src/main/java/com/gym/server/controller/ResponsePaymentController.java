package com.gym.server.controller;

import com.gym.server.dto.Zarinpal.verify.VerifyReqDto;
import com.gym.server.model.InternalPayment;
import com.gym.server.repository.InternalPaymentRepository;
import com.gym.server.repository.UserRepository;
import com.gym.server.service.ZarinpalService;
import com.gym.server.service.impliment.InternalPaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ResponsePaymentController {

    @Value("${app.zarinpal.paymentUrl}")
    private String paymentUrl;


    @Value("${app.ip}")
    private String serverIp;




    @Value("${app.zarinpal.sucessfullRedirectLink}")
    private String successRedirectLink;

    @Value("${app.zarinpal.errorRedirectLink}")
    private String errorRedirectLink;

    private final UserRepository userRepository;



    private final InternalPaymentServiceImpl internalPaymentService;
    private final InternalPaymentRepository internalPaymentRepository;
    private final ZarinpalService zarinpalService;



    @RequestMapping("/api/v1/internalpayment/callback/{transactionId}")
    public String callBack(@PathVariable String transactionId,
                           @RequestParam String Authority,
                           @RequestParam String Status,
                           Model model) {


        if ("NOK".equals(Status)) {
            internalPaymentService.callBackDelete(transactionId);
            model.addAttribute("message", "تراکنش ناموفق");
            model.addAttribute("redirectLink", errorRedirectLink);

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
                model.addAttribute("redirectLink", successRedirectLink);
            } else {
                internalPaymentService.callBackDelete(transactionId);
                model.addAttribute("message", "تراکنش ناموفق");
            }
            model.addAttribute("redirectLink", errorRedirectLink);

        } catch (Exception e) {
//        logger.error("Error processing callback for transactionId: " + transactionId, e);
            model.addAttribute("message", "خطا در پردازش درخواست");
            model.addAttribute("error", e.getMessage());

        }finally {
            return "paymentResult";
        }

    }


}
