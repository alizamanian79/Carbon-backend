package com.gym.server.service.impliment;

import com.gym.server.model.Fee;
import com.gym.server.repository.FeeRepository;
import com.gym.server.service.FeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;

    @Override
    public List<?> getAll() {
        List<Fee> feeList = new ArrayList<>();
        feeRepository.findAll().forEach(f -> feeList.add(f));
        return feeList;
    }

    @Override
    public Fee addFee(Fee req) {
        Fee fee = new Fee();
        fee.setTitle(req.getTitle());
        fee.setDescription(req.getDescription());
        fee.setSessions(req.getSessions());
        fee.setAmount(req.getAmount());
        fee.setDiscount(req.getDiscount());
        return feeRepository.save(fee);
    }

    @Override
    @Transactional
    public Fee updateFee(Fee req) {
        Fee existFee = feeRepository.findById(req.getId()).orElseThrow(() -> new NoSuchElementException("Not found"));
        existFee.setTitle(req.getTitle());
        existFee.setDescription(req.getDescription());
        existFee.setSessions(req.getSessions());
        existFee.setAmount(req.getAmount());
        existFee.setDiscount(req.getDiscount());
        return feeRepository.save(existFee);

    }

    @Override
    public String deleteFee(Long id) {
        Fee existFee = feeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        feeRepository.delete(existFee);
        return "دوره حذف گردید";
    }

    @Override
    public Fee retriveFee(Long id) {
        Fee existFee = feeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        return existFee;
    }
}
