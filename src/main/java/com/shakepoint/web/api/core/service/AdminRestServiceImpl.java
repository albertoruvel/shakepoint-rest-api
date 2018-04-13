package com.shakepoint.web.api.core.service;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.api.core.exception.MandatoryFieldException;
import com.shakepoint.web.api.core.machine.MachineMessageCode;
import com.shakepoint.web.api.core.machine.ProductType;
import com.shakepoint.web.api.core.repository.MachineRepository;
import com.shakepoint.web.api.core.repository.ProductRepository;
import com.shakepoint.web.api.core.repository.PurchaseRepository;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.security.CryptoService;
import com.shakepoint.web.api.core.shop.PayWorksClientService;
import com.shakepoint.web.api.core.shop.PayWorksMode;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.core.util.ValidationUtils;
import com.shakepoint.web.api.data.dto.request.admin.NewMachineRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewProductRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewTechnicianRequest;
import com.shakepoint.web.api.data.dto.response.SimpleMachineProduct;
import com.shakepoint.web.api.data.dto.response.admin.*;
import com.shakepoint.web.api.data.entity.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

@Startup
public class AdminRestServiceImpl implements AdminRestService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MachineRepository machineRepository;

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private CryptoService cryptoService;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.nutritional.tmp", type = ApplicationProperty.Types.SYSTEM)
    private String nutritionalDataTmpFolder;

    @Inject
    private PayWorksClientService payWorksClientService;

    @Inject
    private JmsHandler jmsHandler;

    private static final String MACHINE_CONNECTION_QUEUE_NAME = "machine_connection";
    private static final String DELETE_MEDIA_CONTENT_QUEUE_NAME = "delete_media_content";
    private static final String NUTRITIONAL_DATA_QUEUE_NAME = "nutritional_data";
    private final Logger log = Logger.getLogger(getClass());


    @Override
    public Response getProducts() {
        try {
            List<Product> products = productRepository.getProducts();
            List<SimpleProduct> simpleProducts = TransformationUtils.createSimpleProducts(products);
            return Response.ok(simpleProducts).build();
        } catch (Exception ex) {
            log.error("Could not get products: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Collections.EMPTY_LIST).build();
        }
    }

    @Override
    public Response createNewProduct(MultipartFormDataInput multipart) {
        //validate product
        NewProductRequest product = TransformationUtils.createProductRequestFromMultipart(multipart);
        if (product != null) {
            //validate product
            try{
                ValidationUtils.validateProduct(product);
                Product productEntity = new Product();
                productEntity.setType(ProductType.getProductType(product.getProductType()));
                productEntity = TransformationUtils.createProductFromDto(product);
                productRepository.createProduct(productEntity);
                processFile(multipart, productEntity.getId());
                jmsHandler.send(NUTRITIONAL_DATA_QUEUE_NAME, productEntity.getId());
                //return success
                return Response.ok().build();
            }catch(MandatoryFieldException ex){
                //return validation error
                log.error("Could not create product due to mandatory field is missing", ex);
                return Response.status(Response.Status.BAD_REQUEST)
                        .build();
            }
        } else {
            //return validation error
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public Response getTechnicians() {
        try {
            List<User> techs = userRepository.getTechnicians();
            List<Technician> dtos = TransformationUtils.createTechnicianDtoList(techs);
            return Response.ok(dtos).build();
        } catch (Exception ex) {
            log.error("Could not get technicians: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response createNewTechnician(NewTechnicianRequest dto) {
        User user = TransformationUtils.getUserFromTechnician(dto, cryptoService.encrypt(dto.getPassword()));
        //add the new technician
        try {
            userRepository.addShakepointUser(user);
            return Response.ok().build();
        } catch (Exception ex) {
            log.error("Could not add technician: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response getMachines() {
        try {
            List<VendingMachine> list = machineRepository.getMachines();
            List<SimpleMachine> simpleMachines = TransformationUtils.createSimpleMachines(list, machineRepository);
            return Response.ok(simpleMachines).build();
        } catch (Exception ex) {
            log.error("Error: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response createNewMachine(NewMachineRequest dto) {
        //create a new machine
        VendingMachine machine = TransformationUtils.getMachineFromDTO(dto);
        machine.setTechnician(userRepository.getTechnician(dto.getTechnicianId()));
        try {
            machineRepository.addMachine(machine);
            //start machine
            jmsHandler.send(MACHINE_CONNECTION_QUEUE_NAME, machine.getId());
            return Response.ok().build();
            //TODO: send an email to all super admins
        } catch (Exception ex) {
            log.error("Could not add machine: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response getMachineProducts(String machineId) {
        try {
            List<VendingMachineProductStatus> list = productRepository.getMachineProducts(machineId);
            List<SimpleProduct> dtoList = TransformationUtils.createSimpleProductsFromStatus(list);
            return Response.ok(dtoList).build();
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response getMachineProductsContent(String machineId) {
        MachineProductsContent content = new MachineProductsContent();
        int alertedProducts = machineRepository.getAlertedProducts(machineId);
        List<SimpleMachineProduct> productsPage = TransformationUtils.createSimpleMachineProducts(machineRepository.getMachineProducts(machineId));
        List<SimpleProduct> all = TransformationUtils.createSimpleProducts(productRepository.getProducts());
        VendingMachine machine = machineRepository.getMachine(machineId);
        machine.setProducts(null);//Avoid lazy loading errors when sending this to the UI

        //if the machine has a technician
        Technician technician = null;
        if (machine != null && machine.getTechnician() != null) {
            technician = TransformationUtils.createTechnician(userRepository.getTechnician(machine.getTechnician().getId()));
        }


        List<Technician> technicians = TransformationUtils.createTechnicianDtoList(userRepository.getTechnicians());

        content.setAlertedProducts(alertedProducts);
        content.setMachine(machine);
        content.setMachineProducts(productsPage);
        content.setProducts(all);
        content.setTechnician(technician);
        content.setTechnicians(technicians);
        return Response.ok(content).build();
    }

    @Override
    public Response updateTechnicianMachine(String techId, String machineId, int option) {
        User technician = userRepository.getTechnician(techId);
        VendingMachine machine = machineRepository.getMachine(machineId);
        switch (option) {
            case 0:
                //delete
                machine.setTechnician(null);
                machineRepository.updateMachine(machine);
                return Response.ok().build();
            case 1:
                //add
                machine.setTechnician(technician);
                machineRepository.updateMachine(machine);
                return Response.ok().build();
            default:
                return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Override
    public Response addMachineProduct(String machineId, String productId, int slotNumber) {
        VendingMachineProductStatus newMachineProduct = new VendingMachineProductStatus();
        newMachineProduct.setPercentage(100);
        VendingMachine machine = machineRepository.getMachine(machineId);
        newMachineProduct.setMachine(machine);
        Product product = productRepository.getProduct(productId);
        newMachineProduct.setProduct(product);
        newMachineProduct.setSlotNumber(slotNumber);
        //add the relationship
        newMachineProduct.setUpdatedBy(null);
        // add the new entity
        machineRepository.addMachineProduct(newMachineProduct);

        return Response.ok().build();
    }

    @Override
    public Response deleteMachineProduct(String id) {
        VendingMachineProductStatus status = machineRepository.getMachineProduct(id);
        //delete the machine product
        machineRepository.deleteMachineProduct(id);
        return Response.ok().build();
    }

    @Override
    public Response getPartnerMachines(String techId) {
        List<VendingMachine> list = machineRepository.getTechnicianMachines(techId);
        List<SimpleMachine> dtoList = TransformationUtils.createSimpleMachines(list, machineRepository);
        return Response.ok(dtoList).build();
    }

    @Override
    public Response getTechnicianMachinesContent(String technicianId) {
        TechnicianMachinesContent content = new TechnicianMachinesContent();
        //get technician
        User dto = userRepository.getTechnician(technicianId);
        content.setTechnician(TransformationUtils.createTechnician(dto));
        List<VendingMachine> allMachines = machineRepository.getMachines();
        content.setAllMachines(TransformationUtils.createSimpleMachines(allMachines, machineRepository));
        List<VendingMachine> assignedMachines = machineRepository.getTechnicianMachines(technicianId);
        content.setAsignedMachines(TransformationUtils.createSimpleMachines(assignedMachines, machineRepository));
        return Response.ok(content).build();
    }

    @Override
    public Response getShakepointUsers() {
        try {
            List<User> list = userRepository.getUsers();
            List<UserDescription> descs = new ArrayList();
            for (User user : list) {
                double total = 0;
                for (Purchase purchase : user.getPurchases()) {
                    total += purchase.getTotal();
                }
                descs.add(new UserDescription(user.getId(), user.getName(), user.getEmail(), total));
            }
            return Response.ok(descs).build();
        } catch (Exception ex) {
            log.error("Could not get users", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response getIndexContent() {
        AdminIndexContent content = new AdminIndexContent();
        content.setActiveMachines(machineRepository.getActiveMachines());
        content.setAlertedMachines(machineRepository.getAlertedMachines());
        content.setRegisteredTechnicians(userRepository.getRegisteredTechnicians());
        content.setTodayTotal(purchaseRepository.getTodayTotalPurchases());
        //per machine values
        PerMachineValues perMachine = new PerMachineValues();
        Calendar calendar = Calendar.getInstance();
        //create range dates
        final Date toDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, -7);
        final Date fromDate = calendar.getTime();
        //create a range array
        String[] range = ShakeUtils.getDateRange(fromDate, toDate);
        String from = ShakeUtils.SIMPLE_DATE_FORMAT.format(fromDate);
        String to = ShakeUtils.SIMPLE_DATE_FORMAT.format(toDate);
        perMachine = getIndexPerMachineValuesObject(from, to);

        content.setPerMachineValues(perMachine);
        TotalIncomeValues totalIncome = getTotalIncomeValuesObject(from, to);
        totalIncome.setRange(range);

        content.setTotalIncomeValues(totalIncome);

        PayWorksMode[] modesEnum = PayWorksMode.values();
        String[] modes = new String[modesEnum.length];
        for (int i = 0; i < modesEnum.length; i++) {
            modes[i] = modesEnum[i].getValue();
        }
        content.setModes(modes);
        return Response.ok(content).build();
    }

    @Override
    public Response getIndexPerMachineValues(String from, String to) {
        return Response.ok(getIndexPerMachineValuesObject(from, to)).build();
    }

    @Override
    public Response getTotalIncomeValues(String from, String to) {
        return Response.ok(getTotalIncomeValuesObject(from, to)).build();
    }

    @Override
    public Response deleteMediaContent() {
        log.info("Will delete S3 content, sending JMS message to connector....");
        jmsHandler.send(DELETE_MEDIA_CONTENT_QUEUE_NAME, "Do it!");
        return Response.ok().build();
    }

    @Override
    public Response writePayWorksMode(String mode) {
        //translate mode
        final PayWorksMode currentMode = PayWorksMode.get(mode);
        payWorksClientService.savePayWorksMode(currentMode);
        return Response.ok().build();
    }

    void processFile(MultipartFormDataInput file, final String productId) {
        try (InputStream is = file.getFormDataPart("file", InputStream.class, null)) {
            byte[] bytes = IOUtils.toByteArray(is);
            File tmpFile = new File(nutritionalDataTmpFolder + File.separator + productId + ".jpg");
            FileOutputStream stream = new FileOutputStream(tmpFile);
            stream.write(bytes);
            stream.close();
        } catch (IOException ex) {
            log.error("Could not read file from request", ex);
        } catch (NullPointerException ex) {
            log.error("Provided file is null", ex);
        }

    }

    private PerMachineValues getIndexPerMachineValuesObject(String from, String to) {
        Map<String, List<Double>> map = null;
        PerMachineValues values = new PerMachineValues();
        //get the range
        String[] range = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(from);
            toDate = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(to);
            range = ShakeUtils.getDateRange(fromDate, toDate);
            List<VendingMachine> machines = machineRepository.getMachines();
            map = purchaseRepository.getPerMachineValues(range, machines);
            values.setFromDate(from);
            values.setRange(range);
            values.setToDate(to);
            values.setValues(map);
        } catch (ParseException ex) {
            return null;
        }

        return values;
    }

    private TotalIncomeValues getTotalIncomeValuesObject(String from, String to) {
        TotalIncomeValues values = null;
        List<Double> doubleValues = null;
        String[] range = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(from);
            toDate = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(to);
            range = ShakeUtils.getDateRange(fromDate, toDate);
            //get values
            doubleValues = purchaseRepository.getTotalIncomeValues(range);
            values = new TotalIncomeValues();
            values.setRange(range);
            values.setValues(doubleValues);

        } catch (Exception ex) {
            return null;
        }
        return values;
    }

}
