package com.example.demo.address.dto;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.address.entity.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAddressRequestDTO {

	@NotBlank(message = "Tên đầy đủ không được để trống")
	private String fullName;

	@Pattern(regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])\\d{7}$", message = "Số điện thoại không hợp lệ")
	private String phoneNumber;

	@NotBlank(message = "Địa chỉ chi tiết không được để trống")
	private String addressDetail;

	@NotBlank(message = "Tên thành phố không được để trống")
	private String city;

	@NotBlank(message = "Tên Quận/Huyện không được để trống")
	private String district;

	@NotBlank(message = "Tên Tỉnh/Thành phố không được để trống")
	private String ward;

	Boolean isDefault;

	public Address convertToEntity(UserAccount userAccount) {
		return new Address(this.fullName, this.phoneNumber, this.addressDetail, this.city, this.district, this.ward,
				this.isDefault, userAccount);
	}

}
