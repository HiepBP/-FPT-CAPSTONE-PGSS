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
    
    public partial class CarParkViewModel : SkyWeb.DatVM.Mvc.BaseEntityViewModel<Capstone.Models.Entities.CarPark>
    {
    	
    			public virtual int Id { get; set; }
    			public virtual string Name { get; set; }
    			public virtual string Address { get; set; }
    			public virtual string Phone { get; set; }
    			public virtual string Email { get; set; }
    			public virtual string Description { get; set; }
    			public virtual string Lat { get; set; }
    			public virtual string Lon { get; set; }
    			public virtual Nullable<decimal> Fee { get; set; }
    			public virtual bool Active { get; set; }
    			public virtual string AspNetUserId { get; set; }
    	
    	public CarParkViewModel() : base() { }
    	public CarParkViewModel(Capstone.Models.Entities.CarPark entity) : base(entity) { }
    
    }
}
