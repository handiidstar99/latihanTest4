package com.latihan4.crud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.latihan4.crud.model.*;
import com.latihan4.crud.model.response.RekeningResponse;
import com.latihan4.crud.repository.KaryawanRepo;
import com.latihan4.crud.repository.RekeningRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class RekeningController {
    @Autowired
    private RekeningRepo rekeningRepo;

    @Autowired
    private KaryawanRepo karyawanRepo;

    @PostMapping("/v1/idstar/rekening/save")
    public ResponseEntity<Response> createRekening(@RequestBody Rekening rekening) {

        System.out.println(rekening);

        rekening.setCreatedDate(new Date());

        Optional<Karyawan> optionalEmployee = this.karyawanRepo.findById(rekening.getKaryawan().getId());
        Karyawan karyawan = new Karyawan();

        if (optionalEmployee.isPresent()){
            karyawan = optionalEmployee.get();
        }
        else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Karyawan dengan ID " + rekening.getKaryawan().getId() + " tidak ditemukan.");

            return ResponseEntity.status(response.getCode()).body(response);
        }

        karyawan = karyawanRepo.save(karyawan);
        rekening.setKaryawan(karyawan);
        Rekening createdRekening = rekeningRepo.save(rekening);

        RekeningResponse rekeningResponse = new RekeningResponse();
        rekeningResponse.setId(createdRekening.getId());
        rekeningResponse.setNama(createdRekening.getNama());
        rekeningResponse.setJenis(createdRekening.getJenis());
        rekeningResponse.setRekening(createdRekening.getRekening());
        rekeningResponse.setAlamat(createdRekening.getAlamat());
        rekeningResponse.setCreatedDate(createdRekening.getCreatedDate());
        rekeningResponse.setUpdateDate(createdRekening.getUpdateDate());
        rekeningResponse.setDeletedDate(createdRekening.getDeletedDate());

        Map<String, String> map = new HashMap<>();
        map.put("id", createdRekening.getKaryawan().getId().toString());
        map.put("nama", createdRekening.getKaryawan().getNama());
        rekeningResponse.setKaryawan(map);

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(rekeningResponse);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/v1/idstar/rekening/update")
    public ResponseEntity<Response> updateRekening(@RequestBody Rekening rekening) {

        System.out.println(rekening);

        Optional<Rekening> optionalRekening = this.rekeningRepo.findById(rekening.getId());
        if (optionalRekening.isPresent()){
            Rekening rekeningTemp = optionalRekening.get();
            rekeningTemp.setNama(rekening.getNama());
            rekeningTemp.setJenis(rekening.getJenis());
            rekeningTemp.setRekening(rekening.getRekening());
            rekeningTemp.setAlamat(rekening.getAlamat());

            rekeningTemp.setUpdateDate(new Date());

            Karyawan karyawanTemp = rekening.getKaryawan();
            rekeningTemp.setKaryawan(karyawanTemp);

            rekening = rekeningTemp;
        }
        else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Rekening dengan ID " + rekening.getId() + " tidak ditemukan.");

            return ResponseEntity.status(response.getCode()).body(response);
        }

        rekening.setCreatedDate(new Date());

        Optional<Karyawan> optionalEmployee = this.karyawanRepo.findById(rekening.getKaryawan().getId());
        Karyawan karyawan = new Karyawan();

        System.out.println(optionalEmployee);

        if (optionalEmployee.isPresent()){
            karyawan = optionalEmployee.get();
        }
        else {
            // Prepare the response
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Rekening dengan ID " + rekening.getKaryawan().getId() + " tidak ditemukan.");
            // Prepare the response
            return ResponseEntity.status(response.getCode()).body(response);
        }

        karyawan = karyawanRepo.save(karyawan);
        rekening.setKaryawan(karyawan);
        Rekening createdRekening = rekeningRepo.save(rekening);

        ObjectMapper objectMapper =  new ObjectMapper();
        RekeningResponse rekeningResponse = objectMapper.convertValue(createdRekening, new TypeReference<RekeningResponse>() {
        });

        Map<String, String> map = new HashMap<>();
        map.put("id", createdRekening.getKaryawan().getId().toString());
        map.put("nama", createdRekening.getKaryawan().getNama());
        rekeningResponse.setKaryawan(map);

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(rekeningResponse);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/v1/idstar/rekening/list")
    public ResponseEntity<Response> getRekening(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 1) {
            page = 0;
        }

        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by("id");
        org.springframework.data.domain.Pageable pageable = PageRequest.of(page, size, sort);

        Page<Rekening> rekeningPage = this.rekeningRepo.findAll(pageable);

        com.latihan4.crud.model.Sort sort1 = new com.latihan4.crud.model.Sort();
        sort1.setEmpty(sort.isEmpty());
        sort1.setSorted(sort.isSorted());
        sort1.setUnsorted(sort.isUnsorted());

        com.latihan4.crud.model.Pageable pageable1 = new com.latihan4.crud.model.Pageable();
        pageable1.setPaged(pageable.isPaged());
        pageable1.setSort(sort1);
        pageable1.setOffset(pageable.getOffset());
        pageable1.setPageNumber(pageable.getPageNumber());
        pageable1.setPageSize(pageable.getPageSize());
        pageable1.setUnpaged(pageable.isUnpaged());
        pageable1.setPaged(pageable.isPaged());

        PageList pageList = new PageList();
        pageList.setContent(rekeningPage.getContent());
        pageList.setPageable(pageable1);
        pageList.setLast(rekeningPage.isLast());
        pageList.setTotalElements(rekeningPage.getTotalElements());
        pageList.setTotalPages(rekeningPage.getTotalPages());
        pageList.setSize(rekeningPage.getSize());
        pageList.setNumber(rekeningPage.getNumber());
        pageList.setSort(sort1);
        pageList.setFirst(rekeningPage.isFirst());
        pageList.setNumberOfElements(rekeningPage.getNumberOfElements());
        pageList.setEmpty(rekeningPage.isEmpty());

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(pageList);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/v1/idstar/rekening/{id}")
    public ResponseEntity<Response> getRekeningById(@PathVariable Integer id) {
        Rekening rekening = rekeningRepo.findById(id).orElse(null);

        Response response = new Response();
        if (rekening != null) {
            response.setCode(HttpStatus.OK.value());
            response.setData(rekening);
            response.setStatus("sukses");
        } else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Rekening dengan ID " + id + " tidak ditemukan.");
        }

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/v1/idstar/rekening/delete")
    public ResponseEntity<Response> deleteRekening(@RequestBody Map<String, Integer> request) {
        Integer id = request.get("id");

        Optional<Rekening> optionalRekening = rekeningRepo.findById(id);
        if (optionalRekening.isPresent()) {
            Rekening rekening = optionalRekening.get();
            rekeningRepo.delete(rekening);

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData("Sukses");
            response.setStatus("sukses");
            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Rekening dengan ID " + id + " tidak ditemukan.");
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
