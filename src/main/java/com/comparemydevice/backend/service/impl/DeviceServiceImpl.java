package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.dto.DeviceSpecDTO;
import com.comparemydevice.backend.dto.TagDTO;
import com.comparemydevice.backend.entity.*;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.*;
import com.comparemydevice.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final DeviceSpecRepository specRepo;
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
        Device device = deviceRepo.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));
        return toDeviceDTO(device);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> getAll() {
        return deviceRepo.findAll().stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
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

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> listFiltered(String q, Long brandId, Long categoryId, Long tagId) {
        Page<Device> page = deviceRepo.searchAll(safe(q), brandId, categoryId, tagId, Pageable.unpaged());
        return page.getContent().stream().map(this::toDeviceDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDTO> search(String q, Long brandId, Long categoryId, Long tagId, Pageable pageable) {
        Page<Device> page = deviceRepo.searchAll(safe(q), brandId, categoryId, tagId, pageable);
        List<DeviceDTO> content = page.getContent().stream()
                .map(this::toDeviceDTO)
                .toList();
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return deviceRepo.findDistinctByIdIn(ids).stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(this::toDeviceDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSearchSuggestions(String query) {
        if (query == null || query.isBlank()) return List.of();
        return deviceRepo.findSuggestionsByName(query.trim());
    }

    private static String safe(String q) {
        return (q == null || q.isBlank()) ? null : q.trim();
    }

    // -------------------- Spec helpers --------------------

    @Override
    @Transactional(readOnly = true)
    public List<DeviceSpecDTO> getSpecsForDevice(Long deviceId) {
        return specRepo.findByDevice_Id(deviceId).stream()
                .sorted(Comparator.comparing(ds -> ds.getSpecKey() != null ? ds.getSpecKey().getName() : ""))
                .map(this::toSpecDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Map<Long, String>> compareSpecs(List<Long> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) return Map.of();

        List<Device> devices = deviceRepo.findDistinctByIdIn(deviceIds);
        if (devices.isEmpty()) return Map.of();

        Map<String, Map<Long, String>> table = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Device d : devices) {
            if (Boolean.TRUE.equals(d.getIsDeleted())) continue;
            Long did = d.getId();

            Collection<DeviceSpec> specs = d.getDeviceSpecs() != null ? d.getDeviceSpecs() : List.of();
            for (DeviceSpec s : specs) {
                String keyName = (s.getSpecKey() != null && s.getSpecKey().getName() != null)
                        ? s.getSpecKey().getName() : "—";
                String val = s.getValueText();

                table.computeIfAbsent(keyName, k -> new LinkedHashMap<>())
                        .put(did, val);
            }
        }

        // ensure all devices appear in each row
        for (Map<Long, String> row : table.values()) {
            for (Long id : deviceIds) row.putIfAbsent(id, null);
        }

        return table;
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

    /** Return a Set<Tag> to match the entity field type */
    private Set<Tag> resolveTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return Collections.emptySet();

        List<Tag> fetched = tagRepo.findAllById(tagIds);

        // validate all requested IDs exist
        Set<Long> foundIds = fetched.stream().map(Tag::getId).collect(Collectors.toSet());
        LinkedHashSet<Long> missing = new LinkedHashSet<>(tagIds);
        missing.removeAll(foundIds);
        if (!missing.isEmpty()) {
            throw new ResourceNotFoundException("Tag(s) not found: " + missing);
        }

        // preserve incoming order, remove duplicates, and return a Set
        Map<Long, Tag> byId = fetched.stream().collect(Collectors.toMap(Tag::getId, t -> t));
        LinkedHashSet<Tag> ordered = new LinkedHashSet<>();
        for (Long id : new LinkedHashSet<>(tagIds)) {
            ordered.add(byId.get(id));
        }
        return ordered;
    }

    // -------------------- populate entity --------------------

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
        device.setTags(resolveTags(dto.getTagIds())); // Set<Tag>
    }

    // -------------------- slug --------------------

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

    // -------------------- mappers --------------------

    private DeviceDTO toDeviceDTO(Device d) {
        DeviceDTO dto = mapper.map(d, DeviceDTO.class);
        dto.setBrandId(d.getBrand() != null ? d.getBrand().getId() : null);
        dto.setCategoryId(d.getCategory() != null ? d.getCategory().getId() : null);

        if (d.getTags() != null && !d.getTags().isEmpty()) {
            dto.setTagIds(d.getTags().stream().map(Tag::getId).toList());
            dto.setTags(d.getTags().stream().map(this::toTagDTO).toList());
        }
        if (d.getImages() != null) {
            dto.setImages(d.getImages().stream().map(img -> {
                var i = new com.comparemydevice.backend.dto.ImageDTO();
                i.setId(img.getId());
                i.setUrl(img.getUrl());
                i.setAltText(img.getAltText());
                i.setIsPrimary(img.getIsPrimary());
                i.setSortOrder(img.getSortOrder());
                i.setDeviceId(d.getId());
                return i;
            }).toList());
        }
        if (d.getReviews() != null) {
            dto.setReviews(d.getReviews().stream().map(r -> {
                var rv = new com.comparemydevice.backend.dto.ReviewDTO();
                rv.setId(r.getId());
                rv.setReviewerName(r.getReviewerName());
                rv.setContent(r.getContent());
                rv.setRating(r.getRating());
                rv.setSourceUrl(r.getSourceUrl());
                rv.setDeviceId(d.getId());
                rv.setCreatedAt(r.getCreatedAt());
                rv.setUpdatedAt(r.getUpdatedAt());
                return rv;
            }).toList());
        }
        if (d.getDeviceSpecs() != null) {
            dto.setDeviceSpecs(d.getDeviceSpecs().stream().map(this::toSpecDTO).toList());
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

    private DeviceSpecDTO toSpecDTO(DeviceSpec s) {
        DeviceSpecDTO dto = new DeviceSpecDTO();
        dto.setId(s.getId());
        dto.setDeviceId(s.getDevice() != null ? s.getDevice().getId() : null);
        if (s.getSpecKey() != null) {
            dto.setSpecKeyId(s.getSpecKey().getId());
            dto.setSpecKeyName(s.getSpecKey().getName());
            dto.setSpecType(s.getSpecKey().getSpecType());
        }
        dto.setValueText(s.getValueText());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        return dto;
    }
}