package com.example.demo.address.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.dto.UpdateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.repository.AddressRepository;
import com.example.demo.address.service.AddressService;
import com.example.demo.util.exception.CustomException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AddressService {

	private AddressRepository addressRepository;
	private UserAccountRepository userAccountRepository;

	public AddressService(AddressRepository addressRepository, UserAccountRepository userAccountRepository) {
		this.addressRepository = addressRepository;
		this.userAccountRepository = userAccountRepository;
	}

	@Transactional
	public Address createAddress(@Valid CreateAddressRequestDTO body, String userAccountId) {
		UserAccount user = findUserAccount(userAccountId);

		if (user.getAddresses().size() >= 3) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Người dùng chỉ được tạo tối đa 3 địa chỉ");
		}

		boolean isDefault = body.getIsDefault() || user.getAddresses().isEmpty();

		if (isDefault) {
			for (Address address : user.getAddresses()) {
				address.setIsDefault(false);
			}
		}

		Address myAddress = body.convertToEntity(user);
		myAddress.setIsDefault(isDefault);

		return addressRepository.save(myAddress);
	}

	public List<Address> findAll(String userAccountId) {
		return findUserAccount(userAccountId).getAddresses().stream()
				.sorted((a1, a2) -> Boolean.compare(a2.getIsDefault(), a1.getIsDefault()))
				.toList();
	}

	public Address findById(String addressId, String userAccountId) {
		return findUserAccount(userAccountId).getAddresses().stream()
				.filter(address -> address.getId().equals(addressId)).findFirst()
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại"));
	}

	@Transactional
	public void deleteById(String addressId, String userAccountId) {
		UserAccount user = findUserAccount(userAccountId);

		Address address = user.getAddresses().stream().filter(a -> a.getId().equals(addressId)).findFirst()
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại"));

		boolean wasDefault = address.getIsDefault();

		user.getAddresses().remove(address);

		if (wasDefault && !user.getAddresses().isEmpty()) {
			user.getAddresses().get(0).setIsDefault(true);
		}

		userAccountRepository.save(user);
	}

	@Transactional
	public Address updateById(UpdateAddressRequestDTO body, String addressId, String userAccountId) {
		UserAccount user = findUserAccount(userAccountId);

		List<Address> addresses = user.getAddresses();
		boolean isOnlyOneAddress = addresses.size() == 1;

		Address target = null;

		for (Address address : addresses) {
			if (!address.getId().equals(addressId)) {
				continue;
			}

			target = address;

			if (body.getIsDefault() != null) {

				if (body.getIsDefault() && !address.getIsDefault()) {
					for (Address a : addresses) {
						a.setIsDefault(false);
					}
					address.setIsDefault(true);

				} else if (!body.getIsDefault() && address.getIsDefault()) {
					if (isOnlyOneAddress) {
						throw new CustomException(HttpStatus.BAD_REQUEST, "Không thể bỏ mặc định khi chỉ có 1 địa chỉ");
					}
					address.setIsDefault(false);
				}
			}

			address.update(body.getFullName(), body.getPhoneNumber(), body.getAddressDetail(), body.getCity(),
					body.getDistrict(), body.getWard(), address.getIsDefault());

			break;
		}

		if (target == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ");
		}

		return addressRepository.save(target);
	}

	private UserAccount findUserAccount(String userAccountId) {
		return userAccountRepository.findById(userAccountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}

}
