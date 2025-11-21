package com.l7aur.monitoringmicroservice.repository;

import com.l7aur.monitoringmicroservice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByReferencedDeviceId(Integer deviceId);

    void deleteDeviceByReferencedDeviceId(Integer referencedDeviceId);
}
