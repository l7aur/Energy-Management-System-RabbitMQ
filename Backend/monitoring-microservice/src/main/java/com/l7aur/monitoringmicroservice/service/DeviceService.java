package com.l7aur.monitoringmicroservice.service;

import com.l7aur.monitoringmicroservice.model.Device;
import com.l7aur.monitoringmicroservice.repository.DeviceRepository;
import com.l7aur.monitoringmicroservice.repository.SensorDataRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final SensorDataRepository sensorDataRepository;

    public void create(Device device) {
        try {
            deviceRepository.save(device);
        }
        catch (Exception e) {
            System.out.println("Device: " + device.toString() + " not saved!");
        }
    }

    public Optional<Device> findByReferencedDeviceId(Integer deviceId) {
        return deviceRepository.findByReferencedDeviceId(deviceId);
    }

    @Transactional
    @Modifying
    public void deleteByReferencedId(Integer referencedId) {
        try {
            Optional<Device> d = deviceRepository.findByReferencedDeviceId(referencedId);
            if (d.isEmpty()) {
                System.out.println("Device with id: " + referencedId + " not found!");
                return;
            }
            sensorDataRepository.deleteAllByDevice(d.get());
            deviceRepository.deleteDeviceByReferencedDeviceId(referencedId);
        }
        catch (Exception e) {
            System.out.println("Device identified by referenced id: " + referencedId + " not deleted!");
        }
    }
}
