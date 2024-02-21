package org.example.panda.trabajador.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;

import org.example.panda.feignClient.response.ReniecResponse;
import org.example.panda.trabajador.dtos.TrabajadorDto;
import org.example.panda.trabajador.dtos.TrabajadorResponse;
import org.example.panda.trabajador.services.ITrabajadorService;
import org.example.panda.utilities.AppConstants;
import org.example.panda.utilities.ErrorDetalles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class TrabajadorController {
    private final ITrabajadorService trabajadorService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;

    public TrabajadorController(ITrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }
    @Operation(
            summary = "Gets all trabajadores",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    )
            }
    )
    @GetMapping("/trabajadores")
    public ResponseEntity<TrabajadorResponse> listTrabajadores(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.NUMERO_PAG_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.MEDIDA_PAG_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.ORDENAR_DIRECC_POR_DEFECTO, required = false) String sortDir){
        return new ResponseEntity<>(trabajadorService.listTrabajadores(numeroDePagina,medidaDePagina, ordenarPor, sortDir), HttpStatus.OK);
    }
    @Operation(
            summary = "Get trabajador by id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetalles.class)
                            )
                    )
            }
    )
    @GetMapping("/trabajador/{id}")
    public ResponseEntity<TrabajadorDto> findTrabajadorById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(trabajadorService.listTrabajadorById(id), HttpStatus.OK);
    }
    @Operation(
            summary = "Get reniec by DNI",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetalles.class)
                            )
                    )
            }
    )
    @GetMapping("trabajador/dni/{numero}")
    public ReniecResponse getInfoReniec(@PathVariable String numero){
        return trabajadorService.getInfoReniec(numero);
    }
    @Operation(
            summary = "Create trabajador",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(
                            description = "Bad Request / Invalid data",
                            responseCode = "400"
                    )
                    ,
                    @ApiResponse(
                            description = "Internal Server Error / existing data",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetalles.class)
                            )
                    )
            }
    )
    @PostMapping("trabajador")
    public ResponseEntity<TrabajadorDto> createTrabajadores(@Valid @RequestBody TrabajadorDto trabajadorDto){
        return new ResponseEntity<>(trabajadorService.createTrabajador(trabajadorDto), HttpStatus.CREATED);
    }
    @Operation(
            summary = "Update trabajador",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(
                            description = "Bad Request / Invalid data",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetalles.class)
                            )
                    )
                    ,
                    @ApiResponse(
                            description = "Internal Server Error / existing data",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetalles.class)
                            )
                    )
            }
    )
    @PutMapping("trabajador/{id}")
    public ResponseEntity<TrabajadorDto> updateTrabajador(@Valid @RequestBody TrabajadorDto trabajadorDto, @PathVariable("id") Integer id){
        return new ResponseEntity<>(trabajadorService.updateTrabajador(id, trabajadorDto), HttpStatus.OK);
    }
    @Operation(
            summary = "Delete trabajador",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetalles.class)
                            )
                    )
            }
    )
    @DeleteMapping("trabajador/{id}")
    public ResponseEntity<String> deleteTrabajador(@PathVariable("id") Integer id){
        trabajadorService.deleteTrabajador(id);
        return new ResponseEntity<>("Trabajador eliminado con éxito", HttpStatus.NO_CONTENT);
    }
    @Operation(
            summary = "Get pdf report",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/trabajadores/generar-reporte")
    public void generarPDF(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
        response.setContentType("application/pdf");
        try {
            String ru = resourceLoader.getResource("classpath:panda.jasper").getURI().getPath();

            final File logoEmpresa = ResourceUtils.getFile("classpath:images/logoEmpresa.jpg");
            final File imagenAlternativa = ResourceUtils.getFile("classpath:images/imagenAlternativa.png");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoEmpresa", new FileInputStream(logoEmpresa));
            parameters.put("imagenAlternativa", new FileInputStream(imagenAlternativa));

            JasperPrint jasperPrint = JasperFillManager.fillReport(ru, parameters, dataSource.getConnection());

            OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}