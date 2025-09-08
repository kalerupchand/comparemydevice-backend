// src/main/java/com/comparemydevice/backend/service/impl/DeviceServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.dto.TagDTO;
import com.comparemydevice.backend.entity.Brand;
import com.comparemydevice.backend.entity.Category;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.entity.Tag;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.BrandRepository;
import com.comparemydevice.backend.repository.CategoryRepository;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.repository.TagRepository;
import com.comparemydevice.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepo;
    private final BrandRepository brandRepo;
    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;
    private final ModelMapper mapper;

    // -------------------- CRUD --------------------

    @Override
    @Transactional
    public DeviceDTO create(DeviceDTO dto) {
        Device device = new Device();
        device.setIsDeleted(Boolean.FALSE);

        applyBasics(dto, device);
        applyRelations(dto, device);
        ensureSlug(device, dto.getSlug());

        Device saved = deviceRepo.save(device);
        return toDeviceDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceDTO get(Long id) {
        return toDeviceDTO(getDeviceOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> getAll() {
        return deviceRepo.findAll().stream()
                .map(this::toDeviceDTO)
                .toList();
    }

    @Override
    @Transactional
    public DeviceDTO update(Long id, DeviceDTO dto) {
        Device device = getDeviceOrThrow(id);

        applyBasics(dto, device);
        applyRelations(dto, device);

        if (dto.getSlug() != null && !dto.getSlug().isBlank() && !dto.getSlug().equals(device.getSlug())) {
            device.setSlug(generateUniqueSlug(dto.getSlug()));
        } else if (device.getSlug() == null || device.getSlug().isBlank()) {
            ensureSlug(device, null);
        }

        Device saved = deviceRepo.save(device);
        return toDeviceDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Device device = getDeviceOrThrow(id);
        device.setIsDeleted(Boolean.TRUE);
        deviceRepo.save(device);
    }

    // -------------------- Queries --------------------

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByBrand(Long brandId) {
        requireBrand(brandId);
        return deviceRepo.findByBrand_Id(brandId).stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(this::toDeviceDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByCategory(Long categoryId) {
        requireCategory(categoryId);
        return deviceRepo.findByCategory_Id(categoryId).stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(this::toDeviceDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByTag(Long tagId) {
        requireTag(tagId);
        return deviceRepo.findByTags_Id(tagId).stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(this::toDeviceDTO)
                .toList();
    }

    /** Non-paged list for /api/devices to match the frontend. */
    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> listFiltered(String q, Long brandId, Long categoryId, Long tagId) {
        Page<Device> page = deviceRepo.searchAll(safe(q), brandId, categoryId, tagId, Pageable.unpaged());
        return page.getContent().stream().map(this::toDeviceDTO).toList();
    }

    /** Paged search for /api/devices/search (if you later need paging). */
    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDTO> search(String q, Long brandId, Long categoryId, Long tagId, Pageable pageable) {
        Page<Device> page = deviceRepo.searchAll(safe(q), brandId, categoryId, tagId, pageable);
        // Explicit lambda to avoid method-ref type inference issues in some JDKs
        List<DeviceDTO> content = page.getContent().stream()
                .map(d -> toDeviceDTO(d))
                .toList();
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return deviceRepo.findAllById(ids).stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(this::toDeviceDTO)
                .toList();
    }

    private static String safe(String q) {
        return (q == null || q.isBlank()) ? null : q.trim();
    }

    @Override
    public List<String> getSearchSuggestions(String query) {
        if (query == null || query.isBlank()) return List.of();
        return deviceRepo.findSuggestionsByName(query.trim());
    }

    // -------------------- helpers: load / validate --------------------

    private Device getDeviceOrThrow(Long id) {
        return deviceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));
    }

    private Brand requireBrand(Long id) {
        if (id == null) throw new IllegalArgumentException("brandId is required");
        return brandRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + id));
    }

    private Category requireCategory(Long id) {
        if (id == null) throw new IllegalArgumentException("categoryId is required");
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private Tag requireTag(Long id) {
        if (id == null) throw new IllegalArgumentException("tagId is required");
        return tagRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + id));
    }

    private Set<Tag> resolveTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return Collections.emptySet();
        List<Tag> tags = tagRepo.findAllById(tagIds);
        Set<Long> requested = new HashSet<>(tagIds);
        Set<Long> found = tags.stream().map(Tag::getId).collect(Collectors.toSet());
        if (!found.containsAll(requested)) {
            requested.removeAll(found);
            throw new ResourceNotFoundException("Tag(s) not found: " + requested);
        }
        return new LinkedHashSet<>(tags);
    }

    // -------------------- helpers: populate entity --------------------

    private void applyBasics(DeviceDTO dto, Device device) {
        device.setName(dto.getName());
        device.setProcessor(dto.getProcessor());
        device.setRam(dto.getRam());
        device.setStorage(dto.getStorage());

        device.setPriceAmount(dto.getPriceAmount());
        if (dto.getPriceCurrency() != null && !dto.getPriceCurrency().isBlank()) {
            device.setPriceCurrency(dto.getPriceCurrency());
        }

        device.setReleaseDate(dto.getReleaseDate());

        if (dto.getIsDeleted() != null) {
            device.setIsDeleted(dto.getIsDeleted());
        }
    }

    private void applyRelations(DeviceDTO dto, Device device) {
        device.setBrand(requireBrand(dto.getBrandId()));
        device.setCategory(requireCategory(dto.getCategoryId()));
        device.setTags(resolveTags(dto.getTagIds()));
    }

    // -------------------- helpers: slug --------------------

    private void ensureSlug(Device device, String maybeSlug) {
        if (maybeSlug != null && !maybeSlug.isBlank()) {
            device.setSlug(generateUniqueSlug(maybeSlug));
            return;
        }
        String base = toSlug(device.getName());
        device.setSlug(generateUniqueSlug(base));
    }

    private String generateUniqueSlug(String raw) {
        String base = toSlug(raw);
        String candidate = base;
        int suffix = 1;
        while (deviceRepo.existsBySlug(candidate)) {
            candidate = base + "-" + (++suffix);
        }
        return candidate;
    }

    private static String toSlug(String in) {
        if (in == null) return "item";
        String n = Normalizer.normalize(in, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        n = n.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        return n.isBlank() ? "item" : n;
    }

    // -------------------- mappers: entity → DTO --------------------

    private DeviceDTO toDeviceDTO(Device d) {
        DeviceDTO dto = mapper.map(d, DeviceDTO.class);
        dto.setBrandId(d.getBrand() != null ? d.getBrand().getId() : null);
        dto.setCategoryId(d.getCategory() != null ? d.getCategory().getId() : null);

        if (d.getTags() != null && !d.getTags().isEmpty()) {
            dto.setTagIds(d.getTags().stream().map(Tag::getId).toList());
            dto.setTags(d.getTags().stream().map(this::toTagDTO).toList());
        }
        return dto;
    }

    private TagDTO toTagDTO(Tag t) {
        TagDTO dto = new TagDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setSlug(t.getSlug());
        return dto;
    }
}