package ro.certificate.manager.controller;

import java.net.URL;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ro.certificate.manager.wrapper.CertificateDetails;

@Controller
public class CertificateRetrieverController extends BaseController {
	@RequestMapping(value = "/retrieve_certificates", method = RequestMethod.POST)
	public String retrieve_certificatesPOST(Model model, @RequestParam(value = "URL", required = false) String url,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "host", required = false) String host,
			@RequestParam(value = "port", required = false) Integer port) {

		URL resultedURL = null;
		List<CertificateDetails> certificateDetails = null;
		Exception ex = null;
		try {
			if (type != null) {
				if (type.equals("byURL") && url != null && url.trim().length() > 0) {
					if (!url.startsWith("https")) {
						resultedURL = new URL("https", url, "");
					} else {
						resultedURL = new URL(url);
					}
				} else if (type.equals("byHost") && host != null && host.trim().length() > 0 && port > 0) {
					resultedURL = new URL("https", host, port, "");
				}
			}
			if (resultedURL != null) {
				certificateDetails = certificateGeneratorUtils.retrieveCertificates(resultedURL);
			}
		} catch (Exception e) {
			model.addAttribute("error", "Cannot process your request: " + e.getMessage());
			ex = e;
		}
		if (certificateDetails != null && !certificateDetails.isEmpty()) {
			model.addAttribute("certificatesDetails", certificateDetails);
		} else if (ex == null) {
			model.addAttribute("error", "No certificate was found.");
		}

		return "/parts/certificate_detail";
	}

	@RequestMapping(value = "/retrieve_certificates", method = RequestMethod.GET)
	public String retrieve_certificates(Model model) {
		return "/retrieve_certificates";
	}

}
