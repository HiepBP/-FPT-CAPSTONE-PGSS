//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Capstone.ViewModels
{
    using System;
    using System.Collections.Generic;
    
    public partial class ParkingLotViewModel : SkyWeb.DatVM.Mvc.BaseEntityViewModel<Capstone.Models.Entities.ParkingLot>
    {
    	
    			public virtual int Id { get; set; }
    			public virtual string Name { get; set; }
    			public virtual int Address { get; set; }
    			public virtual int Status { get; set; }
    			public virtual Nullable<int> AreaId { get; set; }
    			public virtual int CarParkId { get; set; }
    			public virtual bool Active { get; set; }
    	
    	public ParkingLotViewModel() : base() { }
    	public ParkingLotViewModel(Capstone.Models.Entities.ParkingLot entity) : base(entity) { }
    
    }
}
