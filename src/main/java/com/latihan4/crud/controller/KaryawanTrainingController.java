package com.latihan4.crud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.latihan4.crud.model.*;
import com.latihan4.crud.model.interfaces.MixIn;
import com.latihan4.crud.model.request.KaryawanTrainingPutRequest;
import com.latihan4.crud.model.request.KaryawanTrainingRequest;
import com.latihan4.crud.model.response.KaryawanResponse;
import com.latihan4.crud.model.response.KaryawanTrainingResponse;
import com.latihan4.crud.repository.KaryawanRepo;
import com.latihan4.crud.repository.KaryawanTrainingRepo;
import com.latihan4.crud.repository.TrainingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class KaryawanTrainingController {
    @Autowired
    private TrainingRepo trainingRepo;

    @Autowired
    private KaryawanTrainingRepo karyawanTrainingRepo;

    @Autowired
    private KaryawanRepo karyawanRepo;

    @PostMapping("/v1/idstar/training/save")
    public ResponseEntity<Response> createTraining(@RequestBody Training training) {

        training.setCreatedDate(new Date());
        Training createdTraining = trainingRepo.save(training);

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(createdTraining);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/v1/idstar/training/update")
    public ResponseEntity<Response> updateTraining(@RequestBody Training training) {

        training.setUpdateDate(new Date());

        Optional<Training> optionalTraining = trainingRepo.findById(training.getId());
        if (optionalTraining.isPresent()) {
            training.setCreatedDate(optionalTraining.get().getCreatedDate());
            Training createdTraining = trainingRepo.save(training);

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData(createdTraining);
            response.setStatus("sukses");

            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            // Prepare the response
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Training dengan ID " + training.getId() + " tidak ditemukan.");

            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @GetMapping("/v1/idstar/training/list")
    public ResponseEntity<Response> getTraining(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 1) {
            page = 0;
        }

        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Training> trainingPage = this.trainingRepo.findAll(pageable);

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(trainingPage);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/v1/idstar/training/{id}")
    public ResponseEntity<Response> getTrainingById(@PathVariable Integer id) {
        Training training = trainingRepo.findById(id).orElse(null);

        Response response = new Response();
        if (training != null) {
            response.setCode(HttpStatus.OK.value());
            response.setData(training);
            response.setStatus("sukses");
        } else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Training dengan ID " + id + " tidak ditemukan.");
        }

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/v1/idstar/training/delete")
    public ResponseEntity<Response> deleteTraining(@RequestBody Map<String, Integer> request) {
        Integer id = request.get("id");

        Optional<Training> optionalTraining = trainingRepo.findById(id);
        if (optionalTraining.isPresent()) {
            Training training = optionalTraining.get();
            trainingRepo.delete(training);

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData("Sukses");
            response.setStatus("sukses");
            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Training dengan ID " + id + " tidak ditemukan.");
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @PostMapping("/v1/idstar/karyawan-training/save")
    public ResponseEntity<Response> createKaryawanTraining(@RequestBody KaryawanTrainingRequest karyawanTrainingRequest) {
        KaryawanTraining karyawanTraining = new KaryawanTraining();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            karyawanTraining.setTanggal(simpleDateFormat.parse(karyawanTrainingRequest.getTanggal()));
        } catch (Exception exception) {
            exception.printStackTrace();

            Response response = new Response();
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus("Format tanggal tidak sesuai (yyyy-MM-dd HH:mm:ss).");
            // Prepare the response
            return ResponseEntity.status(response.getCode()).body(response);
        }

        karyawanTraining.setCreatedDate(new Date());

        Optional<Training> optionalTraining = trainingRepo.findById(karyawanTrainingRequest.getTraining().getId());
        if (optionalTraining.isPresent()) {
            karyawanTraining.setTraining(optionalTraining.get());
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Training dengan ID " + karyawanTrainingRequest.getTraining().getId() + " tidak ditemukan.");
            // Prepare the response
            return ResponseEntity.status(response.getCode()).body(response);
        }

        Optional<Karyawan> optionalKaryawan = karyawanRepo.findById(karyawanTrainingRequest.getKaryawan().getId());
        if (optionalKaryawan.isPresent()) {
            List<Karyawan> karyawanList = new ArrayList<>();
            karyawanList.add(optionalKaryawan.get());
            karyawanTraining.setKaryawan(karyawanList);
        } else {
            // Prepare the response
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Karyawan Training dengan ID " + karyawanTrainingRequest.getKaryawan().getId() + " tidak ditemukan.");
            // Prepare the response
            return ResponseEntity.status(response.getCode()).body(response);
        }

        KaryawanTraining createdKaryawanTraining = this.karyawanTrainingRepo.save(karyawanTraining);
        KaryawanTrainingResponse karyawanTrainingResponse = new KaryawanTrainingResponse();
        karyawanTrainingResponse.setTraining(createdKaryawanTraining.getTraining());

        KaryawanResponse karyawanResponse = new KaryawanResponse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            karyawanResponse = objectMapper.convertValue(createdKaryawanTraining.getKaryawan().get(0), new TypeReference<KaryawanResponse>() {
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        karyawanTrainingResponse.setKaryawan(karyawanResponse);
        karyawanTrainingResponse.setId(createdKaryawanTraining.getId());
        karyawanTrainingResponse.setCreatedDate(createdKaryawanTraining.getCreatedDate());
        karyawanTrainingResponse.setUpdateDate(createdKaryawanTraining.getUpdateDate());
        karyawanTrainingResponse.setDeletedDate(createdKaryawanTraining.getDeletedDate());
        karyawanTrainingResponse.setTanggal(createdKaryawanTraining.getTanggal());

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(karyawanTrainingResponse);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/v1/idstar/karyawan-training/update")
    public ResponseEntity<Response> updateKaryawanTraining(@RequestBody KaryawanTrainingPutRequest karywanTrainingPutRequest) {
        Optional<KaryawanTraining> optionalKaryawanTraining = karyawanTrainingRepo.findById(karywanTrainingPutRequest.getId());
        if (optionalKaryawanTraining.isPresent()) {
            KaryawanTraining karyawanTraining = optionalKaryawanTraining.get();
            karyawanTraining.setUpdateDate(new Date());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                karyawanTraining.setTanggal(simpleDateFormat.parse(karywanTrainingPutRequest.getTanggal()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            Optional<Training> optionalTraining = trainingRepo.findById(karywanTrainingPutRequest.getTraining().getId());
            if (optionalTraining.isPresent()) {
                karyawanTraining.setTraining(optionalTraining.get());
            } else {
                Response response = new Response();
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setStatus("Training dengan ID " + karywanTrainingPutRequest.getTraining().getId() + " tidak ditemukan.");
                return ResponseEntity.status(response.getCode()).body(response);
            }

            Optional<Karyawan> optionalKaryawan = karyawanRepo.findById(karywanTrainingPutRequest.getKaryawan().getId());
            if (optionalKaryawan.isPresent()) {
                List<Karyawan> karyawanList = new ArrayList<>();
                karyawanList.add(optionalKaryawan.get());
                karyawanTraining.setKaryawan(karyawanList);
            } else {
                Response response = new Response();
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setStatus("Karyawan dengan ID " + karywanTrainingPutRequest.getKaryawan().getId() + " tidak ditemukan.");
                return ResponseEntity.status(response.getCode()).body(response);
            }

            KaryawanTraining createdKaryawanTraining = this.karyawanTrainingRepo.save(karyawanTraining);
            KaryawanTrainingResponse karyawanTrainingResponse = new KaryawanTrainingResponse();
            karyawanTrainingResponse.setTraining(createdKaryawanTraining.getTraining());

            KaryawanResponse karyawanResponse = new KaryawanResponse();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                karyawanResponse = objectMapper.convertValue(createdKaryawanTraining.getKaryawan().get(0), new TypeReference<KaryawanResponse>() {
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            karyawanTrainingResponse.setKaryawan(karyawanResponse);
            karyawanTrainingResponse.setId(createdKaryawanTraining.getId());
            karyawanTrainingResponse.setCreatedDate(createdKaryawanTraining.getCreatedDate());
            karyawanTrainingResponse.setUpdateDate(createdKaryawanTraining.getUpdateDate());
            karyawanTrainingResponse.setDeletedDate(createdKaryawanTraining.getDeletedDate());
            karyawanTrainingResponse.setTanggal(createdKaryawanTraining.getTanggal());

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData(karyawanTrainingResponse);
            response.setStatus("sukses");

            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus("Karyawan Training dengan ID " + karywanTrainingPutRequest.getId() + " tidak ditemukan.");
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @GetMapping("/v1/idstar/karyawan-training/list")
    public ResponseEntity<Response> getKaryawanTraining(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 1) {
            page = 0;
        }

        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<KaryawanTraining> karyawanTrainingPage = this.karyawanTrainingRepo.findAll(pageable);

        System.out.println(karyawanTrainingPage);

        List<KaryawanTrainingResponse> karyawanTrainingResponseList = karyawanTrainingPage.stream().map(karyawanTraining -> {
            System.out.println(karyawanTraining);
            KaryawanTrainingResponse karyawanTrainingResponse = new KaryawanTrainingResponse();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setMixInResolver(new ClassIntrospector.MixInResolver() {
                @Override
                public Class<?> findMixInClassFor(Class<?> aClass) {
                    return MixIn.class;
                }

                @Override
                public ClassIntrospector.MixInResolver copy() {
                    return null;
                }
            });
            try {
                karyawanTrainingResponse = objectMapper.convertValue(karyawanTraining, new TypeReference<KaryawanTrainingResponse>() {
                });
                if (karyawanTraining.getKaryawan().size() == 0) {
                    return karyawanTrainingResponse;
                }
                KaryawanResponse karyawanResponse = objectMapper.convertValue(karyawanTraining.getKaryawan().get(0), new TypeReference<KaryawanResponse>() {
                });
                karyawanTrainingResponse.setKaryawan(karyawanResponse);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return karyawanTrainingResponse;
        }).collect(Collectors.toList());

        Page<KaryawanTrainingResponse> karyawanTrainingResponsePage = new PageImpl<>(karyawanTrainingResponseList, pageable, karyawanTrainingResponseList.size());

        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(karyawanTrainingResponsePage);
        response.setStatus("sukses");

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/v1/idstar/karyawan-training/{id}")
    public ResponseEntity<Response> getKaryawanTrainingById(@PathVariable Integer id) {
        KaryawanTraining karyawanTraining = karyawanTrainingRepo.findById(id).orElse(null);

        KaryawanTrainingResponse karyawanTrainingResponse = new KaryawanTrainingResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setMixInResolver(new ClassIntrospector.MixInResolver() {
            @Override
            public Class<?> findMixInClassFor(Class<?> aClass) {
                return MixIn.class;
            }

            @Override
            public ClassIntrospector.MixInResolver copy() {
                return null;
            }
        });
        try {
            karyawanTrainingResponse = objectMapper.convertValue(karyawanTraining, new TypeReference<KaryawanTrainingResponse>() {
            });

            if (karyawanTraining != null) {
                if (karyawanTraining.getKaryawan() != null) {
                    if (karyawanTraining.getKaryawan().size() > 0) {
                        KaryawanResponse karyawanResponse = objectMapper.convertValue(karyawanTraining.getKaryawan().get(0), new TypeReference<KaryawanResponse>() {
                        });
                        karyawanTrainingResponse.setKaryawan(karyawanResponse);
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        Response response = new Response();
        if (karyawanTraining != null) {
            response.setCode(HttpStatus.OK.value());
            response.setData(karyawanTrainingResponse);
            response.setStatus("sukses");
        } else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Karyawan Training dengan ID " + id + " tidak ditemukan.");
        }

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/v1/idstar/karyawan-training/delete")
    public ResponseEntity<Response> deleteKaryawanTraining(@RequestBody Map<String, Integer> request) {
        Integer id = request.get("id");

        Optional<KaryawanTraining> optionalKaryawanTraining = karyawanTrainingRepo.findById(id);
        if (optionalKaryawanTraining.isPresent()) {
            KaryawanTraining karyawanTraining = optionalKaryawanTraining.get();
            karyawanTrainingRepo.delete(karyawanTraining);

            Response response = new Response();
            response.setCode(HttpStatus.OK.value());
            response.setData("Sukses");
            response.setStatus("sukses");
            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            Response response = new Response();
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            response.setStatus("Training dengan ID " + id + " tidak ditemukan.");
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
