package myapp.model;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="data_assets")
public class Data_assets {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer data_asset_id;
	private String assetname;
	private Float assetcost;
	private Integer assetsize;
	private String assetdesc;
	private Boolean featured;
	//private Integer asset_type_id;
	@ManyToOne
	@JoinColumn(name = "asset_type_id")
	@Fetch(FetchMode.JOIN)
	private Asset_types asset_type;

	public Integer getData_asset_id() {
		return data_asset_id;
	}

	public String getAssetname() {
		return assetname;
	}

	public Float getAssetcost() {
		return assetcost;
	}

	public Integer getAssetsize() {
		return assetsize;
	}

	public String getAssetdesc() {
		return assetdesc;
	}

	public Boolean isFeatured() {
		return featured;
	}

	public Asset_types getAsset_Type() {
		return asset_type;
	}

	// public Integer getAsset_type_id() {
	// 	return asset_type_id;
	// }

	public void setData_asset_id(Integer id) {
		data_asset_id = id;
	}

	public void setAssetname(String assetname) {
		this.assetname = assetname;
	}

	public void setAssetcost(Float assetcost) {
		this.assetcost = assetcost;
	}

	public void setAssetsize(Integer assetsize) {
		this.assetsize = assetsize;
	}

	public void setAssetdesc(String assetdesc) {
		this.assetdesc = assetdesc;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public void setAsset_type(Asset_types asset_type) {
		this.asset_type = asset_type;
	}



	// public void setAsset_type_id(Integer id) {
	// 	asset_type_id = id;
	// }
}
