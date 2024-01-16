package com.intakhab.ecommercewebsite.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String from;
    private String to;
    private String subject;
    private String msgBody;
}
