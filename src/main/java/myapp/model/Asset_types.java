package myapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Asset_types {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer asset_type_id;
	private String typename;
	// @OneToMany(targetEntity = Data_assets.class, mappedBy = "data_asset_id", orphanRemoval = false, fetch = FetchType.LAZY)
	// private Set<Data_assets> dataAssets;

	public Integer getAsset_type_id() {
		return asset_type_id;
	}

	public String getTypename() {
		return typename;
	}

	// public Set<Data_assets> getDataAssets() {
	// 	return dataAssets;
	// }

	public void setAsset_type_id(Integer id) {
		asset_type_id = id;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	// public void setData_assets(Set<Data_assets> dataAssets) {
	// 	this.dataAssets = dataAssets;
	// }
}
