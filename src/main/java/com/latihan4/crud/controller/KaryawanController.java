package com.latihan4.crud.controller;

import com.latihan4.crud.model.DetailKaryawan;
import com.latihan4.crud.model.Karyawan;
import com.latihan4.crud.model.PageList;
import com.latihan4.crud.model.Response;
import com.latihan4.crud.repository.DetailKaryawanRepo;
import com.latihan4.crud.repository.KaryawanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
public class KaryawanController {
    @Autowired
    private DetailKaryawanRepo detailKaryawanRepo;

    @Autowired
    private KaryawanRepo empployeeRepo;

    @PostMapping("/v1/idstar/karyawan/save")
    public ResponseEntity<Response> createEmployee(@RequestBody Karyawan karyawan) {

        karyawan.setCreatedDate(new Date());
        DetailKaryawan detailKaryawan = karyawan.getDetailKaryawan();
        detailKaryawan.setCreatedDate(new Date());

        detailKaryawan = detailKaryawanRepo.save(detailKaryawan);
        karyawan.setDetailKaryawan(detailKaryawan);
        Karyawan createdKaryawan = empployeeRepo.save(karyawan);

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(createdKaryawan);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/v1/idstar/karyawan/update")
    public ResponseEntity<Response> updateEmployee(@RequestBody Karyawan karyawan) {

        karyawan.setUpdateDate(new Date());
        DetailKaryawan detailKaryawan = karyawan.getDetailKaryawan();
        detailKaryawan.setUpdateDate(new Date());

        Optional<Karyawan> optionalEmployee = empployeeRepo.findById(karyawan.getId());
        if (optionalEmployee.isPresent()){
            karyawan.setCreatedDate(optionalEmployee.get().getCreatedDate());
            detailKaryawan.setCreatedDate(optionalEmployee.get().getDetailKaryawan().getCreatedDate());

            detailKaryawan = detailKaryawanRepo.save(detailKaryawan);
            karyawan.setDetailKaryawan(detailKaryawan);
            Karyawan createdKaryawan = empployeeRepo.save(karyawan);

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData(createdKaryawan);
            response.setStatus("sukses");

            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Karyawan dengan ID " + karyawan.getId() + " tidak ditemukan.");

            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @GetMapping("/v1/idstar/karyawan/list")
    public ResponseEntity<Response> getEmployee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 1) {
            page = 0;
        }

        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Karyawan> employeePage = this.empployeeRepo.findAll(pageable);

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
        pageList.setContent(employeePage.getContent());
        pageList.setPageable(pageable1);
        pageList.setLast(employeePage.isLast());
        pageList.setTotalElements(employeePage.getTotalElements());
        pageList.setTotalPages(employeePage.getTotalPages());
        pageList.setSize(employeePage.getSize());
        pageList.setNumber(employeePage.getNumber());
        pageList.setSort(sort1);
        pageList.setFirst(employeePage.isFirst());
        pageList.setNumberOfElements(employeePage.getNumberOfElements());
        pageList.setEmpty(employeePage.isEmpty());

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(pageList);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/v1/idstar/karyawan/{id}")
    public ResponseEntity<Response> getEmployeeById(@PathVariable Integer id) {
        Karyawan karyawan = empployeeRepo.findById(id).orElse(null);

        Response response = new Response();
        if (karyawan != null) {
            response.setCode(HttpStatus.OK.value());
            response.setData(karyawan);
            response.setStatus("sukses");
        } else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Karyawan dengan ID " + id + " tidak ditemukan.");
        }

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/v1/idstar/karyawan/delete")
    public ResponseEntity<Response> deleteEmployee(@RequestBody Map<String, Integer> request) {
        Integer id = request.get("id");

        Optional<Karyawan> optionalEmployee = empployeeRepo.findById(id);
        if (optionalEmployee.isPresent()) {
            Karyawan karyawan = optionalEmployee.get();
            empployeeRepo.delete(karyawan);

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData("Sukses");
            response.setStatus("sukses");
            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Karyawan dengan ID " + id + " tidak ditemukan.");
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
