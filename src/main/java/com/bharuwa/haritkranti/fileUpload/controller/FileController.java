/*
 * package com.bharuwa.haritkranti.fileUpload.controller;
 * 
 * import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
 * import com.bharuwa.haritkranti.fileUpload.payload.UploadFileResponse; import
 * com.bharuwa.haritkranti.fileUpload.service.FileStorageService; import
 * com.bharuwa.haritkranti.models.Soil; import
 * com.bharuwa.haritkranti.service.LandService; import org.slf4j.Logger; import
 * org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.core.io.InputStreamResource; import
 * org.springframework.core.io.Resource; import
 * org.springframework.http.MediaType; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.*; import
 * org.springframework.web.multipart.MultipartFile; import
 * org.springframework.web.servlet.support.ServletUriComponentsBuilder;
 * 
 * import java.io.File; import java.io.FileInputStream; import
 * java.io.IOException;
 * 
 * @RestController
 * 
 * @RequestMapping("/api") public class FileController {
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(FileController.class);
 * 
 * @Autowired private FileStorageService fileStorageService;
 * 
 * @Autowired private LandService landService;
 * 
 * // // @PostMapping("/uploadSoilImage") // public UploadFileResponse
 * uploadSoilImage(@RequestParam("file") MultipartFile file , @RequestParam
 * String soilId) { // // String fileName = fileStorageService.storeFile(file);
 * // // Soil soil = landService.getSoil(soilId) ; // if (soil == null) { //
 * throw new ResourceNotFoundException("Soil Not found "); // } //
 * soil.setImageUrl(fileName); // landService.createDefaultSoil(soil); // String
 * fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() //
 * .path("/downloadFile/") // .path(fileName) // .toUriString(); // return new
 * UploadFileResponse(fileName, fileDownloadUri, // file.getContentType(),
 * file.getSize()); // }
 * 
 * // @RequestMapping(value = "/getSoilImage",method =
 * RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE) // @ResponseBody //
 * public ResponseEntity<InputStreamResource> getSoilImage(@RequestParam String
 * imageName) throws IOException { // Resource file =
 * fileStorageService.loadFileAsResource(imageName); // // File
 * newfile=file.getFile(); // FileInputStream input = new
 * FileInputStream(newfile); // // return ResponseEntity // .ok() //
 * .contentType(MediaType.IMAGE_JPEG) // .body(new InputStreamResource(input));
 * // }
 * 
 * 
 * }
 */