package com.hes.postcodescan.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Business {
    @Id @GeneratedValue
    private Long id;
    private @NonNull String name;
    private @NonNull String email;
    private @NonNull String phone;
    private @NonNull String address;
}